package ru.practicum.shoppinglist.feature.listdetail.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Unit
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

interface ProductsRepository {
    fun observeProducts(listId: Long, sortMode: SortMode = SortMode.MANUAL): Flow<List<Product>>

    suspend fun addProduct(listId: Long, name: String, quantity: Double?, unit: Unit?)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(productId: Long)
    suspend fun setPurchased(productId: Long, isPurchased: Boolean)
    suspend fun clearPurchased(listId: Long)
    suspend fun deleteAllProducts(listId: Long)
    suspend fun reorderProducts(listId: Long, fromPosition: Int, toPosition: Int)
    suspend fun copyProductsTo(sourceListId: Long, targetListId: Long)
}
