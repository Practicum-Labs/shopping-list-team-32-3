package ru.practicum.shoppinglist.feature.lists.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList
import ru.practicum.shoppinglist.feature.lists.ui.ListsContract
import ru.practicum.shoppinglist.feature.lists.ui.ListsViewModelBase

class ListsViewModelMock(initial: ListsContract.State) :
    ListsViewModelBase(initial) {
    override fun onIntent(intent: ListsContract.Intent) {}
}

class ListsPreviewProvider : PreviewParameterProvider<ListsViewModelBase> {
    override val values = sequenceOf(
        ListsViewModelMock(
            ListsContract.State(
                lists = listOf(ShoppingList.mock()),
                isLoading = false,
                activeSheet = null,
                query = "123",
                error = null,
            )
        )
    )
}
