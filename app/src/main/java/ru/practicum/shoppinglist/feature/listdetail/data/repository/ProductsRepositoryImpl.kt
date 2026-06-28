package ru.practicum.shoppinglist.feature.listdetail.data.repository

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.practicum.shoppinglist.core.domain.exception.DataException
import ru.practicum.shoppinglist.feature.listdetail.data.dao.ProductDao
import ru.practicum.shoppinglist.feature.listdetail.data.toDomainList
import ru.practicum.shoppinglist.feature.listdetail.data.toEntity
import ru.practicum.shoppinglist.feature.listdetail.domain.api.ProductsRepository
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

@Suppress("SwallowedException")
class ProductsRepositoryImpl(
    private val dao: ProductDao
) : ProductsRepository {

    override fun observeProducts(
        listId: Long,
        sortMode: SortMode
    ): Flow<List<Product>> {
        return when (sortMode) {
            SortMode.MANUAL -> dao.observeByListOrderedByPosition(listId)
            SortMode.ALPHABETICAL -> dao.observeByListOrderedByName(listId)
        }.map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun addProduct(
        listId: Long,
        name: String,
        quantity: Double?,
        unit: ProductUnit?
    ) {
        try {
            val maxPosition = dao.getMaxPosition(listId) ?: -1
            val product = Product(
                id = 0,
                listId = listId,
                name = name,
                quantity = quantity,
                unit = unit,
                isPurchased = false,
                position = maxPosition + 1
            )
            dao.upsert(product.toEntity())
        } catch (e: SQLiteException) {
            throw DataException.Database(e.message.toString())
        } catch (e: IllegalArgumentException) {
            throw DataException.InvalidData(e.message.toString())
        }
    }

    override suspend fun updateProduct(product: Product) {
        try {
            dao.update(product.toEntity())
        } catch (e: SQLiteException) {
            throw DataException.Database(e.message.toString())
        } catch (e: IllegalArgumentException) {
            throw DataException.InvalidData(e.message.toString())
        }
    }

    override suspend fun deleteProduct(productId: Long) {
        try {
            dao.delete(productId)
        } catch (e: SQLiteException) {
            throw DataException.Database(e.message.toString())
        } catch (e: IllegalArgumentException) {
            throw DataException.InvalidData(e.message.toString())
        }
    }

    override suspend fun setPurchased(productId: Long, isPurchased: Boolean) {
        try {
            dao.setPurchased(productId, isPurchased)
        } catch (e: SQLiteException) {
            throw DataException.Database(e.message.toString())
        } catch (e: IllegalArgumentException) {
            throw DataException.InvalidData(e.message.toString())
        }
    }

    override suspend fun clearPurchased(listId: Long) {
        try {
            dao.clearPurchased(listId)
        } catch (e: SQLiteException) {
            throw DataException.Database(e.message.toString())
        } catch (e: IllegalArgumentException) {
            throw DataException.InvalidData(e.message.toString())
        }
    }

    override suspend fun deleteAllProducts(listId: Long) {
        try {
            dao.deleteByList(listId)
        } catch (e: SQLiteException) {
            throw DataException.Database(e.message.toString())
        } catch (e: IllegalArgumentException) {
            throw DataException.InvalidData(e.message.toString())
        }
    }

    override suspend fun reorderProducts(
        listId: Long,
        fromPosition: Int,
        toPosition: Int
    ) {
        if (fromPosition == toPosition) return
    }

    override suspend fun copyProductsTo(sourceListId: Long, targetListId: Long) {
        val products = dao.observeByListOrderedByPosition(sourceListId).first()
        products.forEach { entity ->
            dao.upsert(
                entity.copy(
                    id = 0,
                    listId = targetListId,
                    isPurchased = false,
                    position = entity.position
                )
            )
        }
    }
}
