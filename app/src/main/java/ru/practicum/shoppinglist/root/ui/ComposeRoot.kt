package ru.practicum.shoppinglist.root.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppModalBottomSheet
import ru.practicum.shoppinglist.core.ui.components.PrimaryButton
import ru.practicum.shoppinglist.core.ui.navigation.NavGraph
import ru.practicum.shoppinglist.core.ui.theme.AppTheme

@Composable
fun ComposeRoot(viewModel: RootViewModel, onInit: () -> Unit) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.value.initialState) {
        state.value.initialState?.let {
            onInit()
        }
    }

    AppTheme {
        key(state.value.recompositionTrigger) {
            val navController = rememberNavController()
            state.value.initialState?.let {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        NavGraph(navController, it)
                    }
                }
            }
        }

        if (state.value.showDebugMenu) {
            AppModalBottomSheet({ viewModel.onIntent(RootContract.Intent.CloseDebugMenu) }) {
                DebugMenu(
                    onFakeLogin = {
                        viewModel.onIntent(RootContract.Intent.FakeLogin)
                    }
                )
            }
        }
    }
}

@Composable
private fun DebugMenu(onFakeLogin: () -> Unit) {
    Column {
        PrimaryButton(
            R.string.root_debug_fake_login,
            onFakeLogin
        )
    }
}
