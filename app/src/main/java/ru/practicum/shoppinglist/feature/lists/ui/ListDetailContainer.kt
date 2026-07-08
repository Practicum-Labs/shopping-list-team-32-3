package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.EmptyState
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailScreen
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.ListDetailPreviewProvider
import ru.practicum.shoppinglist.feature.lists.ui.preview.ListDetailContainerPreviewProvider

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListDetailContainer(
    initialId: Long? = null
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()
    val scope = rememberCoroutineScope()

    LaunchedEffect(initialId) {
        if (initialId != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, initialId)
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                ListsScreen(
                    onNavigateToDetail = { id ->
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val currentId = navigator.currentDestination?.contentKey

                if (currentId != null) {
                    key(currentId) {
                        ListDetailScreen(
                            viewModel = koinViewModel { parametersOf(currentId) }
                        ) {
                            scope.launch {
                                navigator.navigateBack()
                            }
                        }
                    }
                } else {
                    EmptyState(
                        R.drawable.image_listdetail_empty_state,
                        R.string.lists_container_empty_title,
                        R.string.lists_container_empty_description,
                        modifier = Modifier
                            .padding(horizontal = Dimens.padding44)
                            .padding(top = Dimens.padding120)
                    )
                }
            }
        }
    )
}

@AppPreview
@Composable
private fun ListDetailContainerPreview(
    @PreviewParameter(ListDetailContainerPreviewProvider::class) model: Long?
) {
    AppTheme {
        ListDetailContainer(model)
    }
}

