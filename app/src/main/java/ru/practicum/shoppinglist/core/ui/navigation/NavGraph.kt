package ru.practicum.shoppinglist.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.viewmodel.koinViewModel
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailScreen
import ru.practicum.shoppinglist.feature.lists.ui.ListsScreen
import ru.practicum.shoppinglist.feature.onboarding.ui.OnboardingScreen

@Composable
fun NavGraph(navController: NavHostController, passedOnboarding: Boolean) {
    NavHost(navController, startDestination = if (passedOnboarding) Screen.Lists else Screen.Onboarding) {
        composable<Screen.Onboarding> {
            OnboardingScreen {
                navController.navigate(Screen.Lists)
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
    }
}
