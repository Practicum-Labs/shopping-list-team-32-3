package ru.practicum.shoppinglist.feature.listdetail.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailContract
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

class ListDetailViewModelMock(initial: ListDetailContract.State) :
    ListDetailViewModelBase(initial) {
    override fun onIntent(intent: ListDetailContract.Intent) {}
}

class ListDetailPreviewProvider : PreviewParameterProvider<ListDetailViewModelBase> {
    override val values = sequenceOf(
        ListDetailViewModelMock(
            ListDetailContract.State(
                listName = "listName",
                products = emptyList(),
                sortMode = SortMode.ALPHABETICAL,
                isLoading = true
            )
        ),
        ListDetailViewModelMock(
            ListDetailContract.State(
                listId = 1,
                listName = "listName",
                products = emptyList(),
                sortMode = SortMode.ALPHABETICAL,
                isLoading = false
            )
        ),
        ListDetailViewModelMock(
            ListDetailContract.State(
                listId = 1,
                listName = "listName",
                products = listOf(Product.mock1(), Product.mock2()),
                sortMode = SortMode.ALPHABETICAL,
                isLoading = false
            )
        )
    )
}
