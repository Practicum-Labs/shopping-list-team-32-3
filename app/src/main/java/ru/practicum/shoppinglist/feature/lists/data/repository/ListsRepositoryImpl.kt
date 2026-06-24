package ru.practicum.shoppinglist.feature.lists.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity
import ru.practicum.shoppinglist.feature.lists.data.toDomainList
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository
import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList

class ListsRepositoryImpl(
    private val dao: ListDao
) : ListsRepository {

    override fun observeLists(): Flow<List<ShoppingList>> =
        dao.observeAll().map { entities -> entities.toDomainList() }

    override suspend fun create(name: String) {
        dao.upsert(ListEntity(name = name))
    }

    override suspend fun rename(id: Long, name: String) {
        dao.rename(id, name)
    }

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
