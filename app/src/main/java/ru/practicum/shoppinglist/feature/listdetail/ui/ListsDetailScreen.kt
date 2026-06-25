package ru.practicum.shoppinglist.feature.listdetail.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AddFab
import ru.practicum.shoppinglist.core.ui.components.AppModalBottomSheet
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.EmptyState
import ru.practicum.shoppinglist.core.ui.components.TopAppBar
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.listdetail.ui.components.ProductCard
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.ListDetailPreviewProvider

@Composable
fun ListDetailScreen(
    viewModel: ListDetailViewModelBase,
    onBack: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var isSheetVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ListDetailContract.Effect.NavigateBack -> {
                    onBack()
                }
                is ListDetailContract.Effect.NavigateToProductDetail -> {
                    isSheetVisible = true
                }
                is ListDetailContract.Effect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (isSheetVisible) {
        AppModalBottomSheet(
            onDismiss = {
                isSheetVisible = false
            }
        ) {
            Text("TODO")
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                state.value.listName,
                onBack,
                actions = { ListDetailMenuIcon({}) }
            )
        },
        floatingActionButton = {
            if (!state.value.isLoading) {
                AddFab(
                    {},
                    modifier = Modifier.padding(
                        end = Dimens.padding16,
                        bottom = Dimens.padding56
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()

        ) {
            if (!state.value.isLoading) {
                if (state.value.products.isEmpty()) {
                    Column {
                        EmptyState(
                            R.drawable.image_listdetail_empty_state,
                            R.string.listdetail_empty_state_title,
                            R.string.listdetail_empty_state_description,
                            modifier = Modifier
                                .padding(horizontal = Dimens.padding44)
                                .padding(top = Dimens.padding120)
                        )
                        Spacer(modifier = Modifier.height(0.dp).weight(1f))
                    }
                } else {
                    LazyColumn {
                        items(state.value.products, key = { it.id }) { product ->
                            ProductCard(product, {}, {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListDetailMenuIcon(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(
                id = R.drawable.ic_core_menu_icon
            ),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
        )
    }
}

@AppPreview
@Composable
fun ListDetailScreenPreview(
    @PreviewParameter(ListDetailPreviewProvider::class) model: ListDetailViewModelBase
) {
    AppTheme {
        ListDetailScreen(model) {}
    }
}
