package ru.practicum.shoppinglist.feature.lists.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.ListDetailPreviewProvider
import ru.practicum.shoppinglist.feature.lists.ui.DetailViewModelFactory
import ru.practicum.shoppinglist.feature.lists.ui.ListsViewModelBase

data class ListDetailContainerPreviewData(
    val listsViewModel: ListsViewModelBase,
    val detailViewModelFactory: DetailViewModelFactory,
    val initialId: Long?
)
class ListDetailContainerPreviewProvider : PreviewParameterProvider<ListDetailContainerPreviewData> {
    override val values = sequenceOf(
        ListDetailContainerPreviewData(
            listsViewModel = ListsPreviewProvider().values.first(),
            detailViewModelFactory = MockDetailViewModelFactory(),
            initialId = null
        ),
        ListDetailContainerPreviewData(
            listsViewModel = ListsPreviewProvider().values.first(),
            detailViewModelFactory = MockDetailViewModelFactory(),
            initialId = 1
        )
    )
}

class MockDetailViewModelFactory : DetailViewModelFactory {
    override fun create(id: Long): ListDetailViewModelBase = ListDetailPreviewProvider().values.first()
}
