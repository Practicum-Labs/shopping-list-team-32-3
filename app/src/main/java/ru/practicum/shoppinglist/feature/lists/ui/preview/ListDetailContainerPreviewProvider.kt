package ru.practicum.shoppinglist.feature.lists.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailContract
import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.mock1
import ru.practicum.shoppinglist.feature.listdetail.ui.preview.mock2
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode


class ListDetailContainerPreviewProvider : PreviewParameterProvider<Long?> {
    override val values = sequenceOf(
        null,
        1L
    )
}
