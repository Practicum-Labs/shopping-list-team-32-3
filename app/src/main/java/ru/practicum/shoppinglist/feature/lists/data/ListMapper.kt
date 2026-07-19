package ru.practicum.shoppinglist.feature.lists.data

import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity
import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

fun ListEntity.toDomain(): ShoppingList = ShoppingList(
    id = id,
    userId = userId,
    name = name,
    iconKey = iconKey,
    sortMode = runCatching { SortMode.valueOf(sortMode.uppercase()) }
        .getOrDefault(SortMode.MANUAL)
)

fun ShoppingList.toEntity(): ListEntity = ListEntity(
    id = id,
    userId = userId,
    name = name,
    iconKey = iconKey,
    sortMode = sortMode.name.lowercase(),
)

fun List<ListEntity>.toDomainList(): List<ShoppingList> = map { it.toDomain() }
