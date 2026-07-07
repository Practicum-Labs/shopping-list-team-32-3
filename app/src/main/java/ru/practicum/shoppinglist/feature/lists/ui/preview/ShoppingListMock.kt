package ru.practicum.shoppinglist.feature.lists.ui.preview

import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

fun ShoppingList.Companion.mock(): ShoppingList {
    return ShoppingList(
        id = 1,
        name = "Список продуктов",
        iconKey = "",
        sortMode = SortMode.ALPHABETICAL
    )
}
