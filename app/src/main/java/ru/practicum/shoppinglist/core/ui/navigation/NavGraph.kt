package ru.practicum.shoppinglist.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import ru.practicum.shoppinglist.feature.auth.ui.LoginScreen
import ru.practicum.shoppinglist.feature.auth.ui.RecoveryScreen
import ru.practicum.shoppinglist.feature.auth.ui.RegistrationScreen
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailScreen
import ru.practicum.shoppinglist.feature.lists.ui.ListsScreen
import ru.practicum.shoppinglist.feature.onboarding.ui.OnboardingScreen
import ru.practicum.shoppinglist.root.ui.InitialState

@Composable
fun NavGraph(navController: NavHostController, initialState: InitialState) {
    NavHost(
        navController,
        startDestination = when (initialState) {
            InitialState.ONBOARDING -> Screen.Onboarding
            InitialState.AUTH -> Screen.Login
            InitialState.CONTENT -> Screen.Lists
        }
    ) {
        composable<Screen.Onboarding> {
            OnboardingScreen {
                navController.navigate(Screen.Login) {
                    popUpTo(Screen.Onboarding) { inclusive = true }
                }
            }
        }
        composable<Screen.Lists> {
            ListsScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.ListDetail(id))
                },
            )
        }
        composable<Screen.ListDetail> {
            // val args = it.toRoute<Screen.ListDetail>()
            ListDetailScreen(
                koinViewModel()
            ) {
                navController.popBackStack()
            }
        }
        composable<Screen.Login> {
            LoginScreen(
                onNavigateToRecovery = {
                    navController.navigate(Screen.PasswordRecovery)
                },
                onNavigateToRegistration = {
                    navController.navigate(Screen.Registration)
                },
                onNavigateToLists = {
                    navController.navigate(Screen.Lists) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.PasswordRecovery> {
            RecoveryScreen {
                navController.popBackStack()
            }
        }
        composable<Screen.Registration> {
            RegistrationScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToLists = {
                    navController.navigate(Screen.Lists) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }
    }
}
