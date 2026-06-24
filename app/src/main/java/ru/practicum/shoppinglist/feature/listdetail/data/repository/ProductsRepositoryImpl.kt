package ru.practicum.shoppinglist.feature.listdetail.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.practicum.shoppinglist.feature.listdetail.data.dao.ProductDao
import ru.practicum.shoppinglist.feature.listdetail.data.toDomainList
import ru.practicum.shoppinglist.feature.listdetail.data.toEntity
import ru.practicum.shoppinglist.feature.listdetail.domain.api.ProductsRepository
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Unit
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

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
        unit: Unit?
    ) {
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
    }

    override suspend fun updateProduct(product: Product) {
        dao.update(product.toEntity())
    }

    override suspend fun deleteProduct(productId: Long) {
        dao.delete(productId)
    }

    override suspend fun setPurchased(productId: Long, isPurchased: Boolean) {
        dao.setPurchased(productId, isPurchased)
    }

    override suspend fun clearPurchased(listId: Long) {
        dao.clearPurchased(listId)
    }

    override suspend fun deleteAllProducts(listId: Long) {
        dao.deleteByList(listId)
    }

    override suspend fun setSortMode(
        listId: Long,
        sortMode: SortMode
    ) {
    }

    override suspend fun reorderProducts(
        listId: Long,
        fromPosition: Int,
        toPosition: Int
    ) {
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
