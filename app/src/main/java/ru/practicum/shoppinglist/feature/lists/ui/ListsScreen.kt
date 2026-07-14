package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AddFab
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.Dialog
import ru.practicum.shoppinglist.core.ui.components.EmptyState
import ru.practicum.shoppinglist.core.ui.components.FullScreenLoader
import ru.practicum.shoppinglist.core.ui.components.TopAppBar
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.lists.ui.preview.ListsPreviewProvider

@Composable
fun ListsScreen(
    onNavigateToDetail: (id: Long) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: ListsViewModelBase
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ListsContract.Effect.OpenList -> onNavigateToDetail(effect.id)
                is ListsContract.Effect.ShowLogoutDialog -> showDialog.value = true
                is ListsContract.Effect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    ListsContent(
        state = state,
        onIntent = viewModel::onIntent,
    )

    if (showDialog.value) {
        Dialog(
            title = stringResource(R.string.lists_logout_dialog_title),
            confirmTitle = stringResource(R.string.lists_logout_dialog_confirm),
            onConfirm = {
                viewModel.onIntent(ListsContract.Intent.Logout)
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListsContent(
    state: ListsContract.State,
    onIntent: (ListsContract.Intent) -> Unit,
) {
    FullScreenLoader(isLoading = state.isLoading) {
        Scaffold(
            topBar = {
                TopAppBar(
                    stringResource(R.string.lists_title),
                    actions = {
                        IconButton(onClick = { onIntent(ListsContract.Intent.OpenLogoutConfirm) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lists_exit_icon),
                                contentDescription = null,
                                modifier = Modifier.size(Dimens.icon32)
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                AddFab(onClick = { onIntent(ListsContract.Intent.OpenAddSheet) })
            },
        ) { padding ->
            when {
                state.lists.isEmpty() && !state.isLoading -> {
                    EmptyState(
                        imageId = R.drawable.image_lists_empty_state,
                        titleId = R.string.lists_empty_title,
                        descriptionId = R.string.lists_empty_description,
                        modifier = Modifier
                            .padding(padding)
                            .padding(Dimens.padding44),
                    )
                }

                else -> {
                    val listState = rememberLazyListState()
                    val openCardId = remember { mutableStateOf<Long?>(null) }
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = padding.calculateTopPadding() + Dimens.padding16,
                            bottom = padding.calculateBottomPadding() + Dimens.padding16,
                            start = Dimens.padding16,
                            end = Dimens.padding16,
                        ),
                        verticalArrangement = Arrangement.spacedBy(Dimens.padding16),
                    ) {
                        items(state.lists, key = { it.id }) { list ->
                            SwipeableListCard(
                                list = list,
                                onClick = { onIntent(ListsContract.Intent.OpenList(list.id)) },
                                onIconClick = { onIntent(ListsContract.Intent.OpenIconsSheet(list.id)) },
                                onRename = { onIntent(ListsContract.Intent.RenameList(list.id)) },
                                onDuplicate = { onIntent(ListsContract.Intent.DuplicateList(list.id)) },
                                onDelete = { onIntent(ListsContract.Intent.RequestDelete(list.id)) },
                                collapse = openCardId.value != null && openCardId.value != list.id,
                                onExpandedChange = { expanded ->
                                    openCardId.value =
                                        resolveOpenCardId(openCardId.value, list.id, expanded)
                                },
                                resetSignal = listState.isScrollInProgress to state.swipeResetToken,
                            )
                        }
                    }
                }
            }
        }
        if (state.activeSheet != null) {
            ListsSheet(state.activeSheet, onIntent)
        }
    }
}

@Composable
private fun ListsSheet(
    sheet: ListsContract.Sheet,
    onIntent: (ListsContract.Intent) -> Unit,
) {
    when (sheet) {
        is ListsContract.Sheet.AddList -> ListNameSheet(
            titleId = R.string.lists_add_title,
            confirmLabelId = R.string.lists_add_button_create,
            onDismiss = { onIntent(ListsContract.Intent.DismissSheet) },
            onConfirm = { onIntent(ListsContract.Intent.CreateList(it)) },
        )

        is ListsContract.Sheet.Rename -> ListNameSheet(
            titleId = R.string.lists_rename_title,
            confirmLabelId = R.string.lists_rename_button,
            initialName = sheet.currentName,
            onDismiss = { onIntent(ListsContract.Intent.DismissSheet) },
            onConfirm = { onIntent(ListsContract.Intent.ConfirmRename(sheet.id, it)) },
        )

        is ListsContract.Sheet.SelectIcon -> ListIconsBottomSheet(
            onDismiss = { onIntent(ListsContract.Intent.DismissSheet) },
            onSelect = { onIntent(ListsContract.Intent.ChangeIcon(it.value, sheet.id)) },
        )
    }
}

private fun resolveOpenCardId(current: Long?, cardId: Long, expanded: Boolean): Long? = when {
    expanded -> cardId
    current == cardId -> null
    else -> current
}

@AppPreview
@Composable
private fun ListsScreenPreview(
    @PreviewParameter(ListsPreviewProvider::class) model: ListsViewModelBase
) {
    AppTheme {
        ListsScreen({}, {}, model)
    }
}
