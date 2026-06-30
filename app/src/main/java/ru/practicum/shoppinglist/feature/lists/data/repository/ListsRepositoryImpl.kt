package ru.practicum.shoppinglist.feature.lists.data.repository

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.shoppinglist.core.domain.exception.DataException
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity
import ru.practicum.shoppinglist.feature.lists.data.toDomain
import ru.practicum.shoppinglist.feature.lists.data.toDomainList
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository
import ru.practicum.shoppinglist.feature.lists.domain.models.ShoppingList
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

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

    override suspend fun getListById(listId: Long): ShoppingList? {
        val entity = dao.getListById(listId) ?: return null
        return entity.toDomain()
    }

    override suspend fun setSortMode(
        listId: Long,
        sortMode: SortMode
    ) {
        try {
            dao.updateSortMode(listId, sortMode.name)
        } catch (e: SQLiteException) {
            throw DataException.Database(e.message.toString(), e)
        } catch (e: IllegalArgumentException) {
            throw DataException.InvalidData(e.message.toString(), e)
        }
    }
}
