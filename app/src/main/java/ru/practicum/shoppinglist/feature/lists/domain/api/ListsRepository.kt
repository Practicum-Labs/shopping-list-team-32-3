package ru.practicum.shoppinglist.feature.lists.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

interface ListsRepository {
    fun observeLists(): Flow<List<ShoppingList>>

    suspend fun create(name: String)
    suspend fun rename(id: Long, name: String)
    suspend fun delete(id: Long)
    suspend fun deleteAll()
    suspend fun getListById(listId: Long): ShoppingList?
    suspend fun setSortMode(listId: Long, sortMode: SortMode)
}
