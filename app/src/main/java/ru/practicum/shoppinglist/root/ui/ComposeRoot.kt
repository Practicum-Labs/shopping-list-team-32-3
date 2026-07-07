package ru.practicum.shoppinglist.root.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import ru.practicum.shoppinglist.core.ui.navigation.NavGraph
import ru.practicum.shoppinglist.core.ui.theme.AppTheme

@Composable
fun ComposeRoot(viewModel: RootViewModel, onInit: () -> Unit) {
    val navController = rememberNavController()
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.value.initialState) {
        state.value.initialState?.let {
            onInit()
        }
    }

    state.value.initialState?.let {
        AppTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    NavGraph(navController = navController, it)
                }
            }
        }
    }
}
