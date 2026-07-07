package ru.practicum.shoppinglist.feature.lists.domain.models

// Список покупок
// Именованный набор товаров с иконкой. Пользователь может иметь несколько списков.
data class ShoppingList(
    val id: Long,
    val userId: Long,
    val name: String,
    // Иконка списка
    // Значок из набора Material Symbols, назначаемый списку для визуального различения.
    val iconKey: String,
    val sortMode: SortMode
) {
    companion object
}

typealias ShoppingLists = List<ShoppingList>
