package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AddFab
import ru.practicum.shoppinglist.core.ui.components.EmptyState
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun ListsScreen(
    onNavigateToDetail: (id: Long) -> Unit,
    viewModel: ListsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ListsContract.Effect.OpenList -> onNavigateToDetail(effect.id)
            }
        }
    }

    ListsContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListsContent(
    state: ListsContract.State,
    onIntent: (ListsContract.Intent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.lists_title)) })
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
                            onRename = { onIntent(ListsContract.Intent.RenameList(list.id)) },
                            onDuplicate = { onIntent(ListsContract.Intent.DuplicateList(list.id)) },
                            onDelete = { onIntent(ListsContract.Intent.RequestDelete(list.id)) },
                            resetSignal = listState.isScrollInProgress,
                        )
                    }
                }
            }
        }
    }

    if (state.activeSheet is ListsContract.Sheet.AddList) {
        AddListSheet(
            onDismiss = { onIntent(ListsContract.Intent.DismissSheet) },
            onCreate = { onIntent(ListsContract.Intent.CreateList(it)) },
        )
    }
}
