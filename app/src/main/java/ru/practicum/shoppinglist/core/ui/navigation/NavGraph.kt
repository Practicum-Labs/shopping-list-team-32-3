package ru.practicum.shoppinglist.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailScreen
import ru.practicum.shoppinglist.feature.lists.ui.ListsScreen
import ru.practicum.shoppinglist.feature.onboarding.ui.OnboardingScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Onboarding) {
        composable<Screen.Onboarding> {
            OnboardingScreen {
                navController.navigate(Screen.Lists)
            }
        }
        composable<Screen.Lists> {
            ListsScreen {
                navController.navigate(Screen.ListDetail(1))
            }
        }
        composable<Screen.ListDetail> {
            // val args = it.toRoute<Screen.ListDetail>()
            ListDetailScreen {
                navController.popBackStack()
            }
        }
    }
}
