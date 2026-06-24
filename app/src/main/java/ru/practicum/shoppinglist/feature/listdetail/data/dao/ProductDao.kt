package ru.practicum.shoppinglist.feature.listdetail.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.practicum.shoppinglist.feature.listdetail.data.entity.ProductEntity

@Dao
interface ProductDao {
    fun observeByList(listId: Long): Flow<List<ProductEntity>> =
        observeByListOrderedByPosition(listId)

    @Query(
        """        
        SELECT * FROM products 
        WHERE listId = :listId 
        ORDER BY position ASC, id ASC
        """
    )
    fun observeByListOrderedByPosition(listId: Long): Flow<List<ProductEntity>>

    @Query(
        """
        SELECT * FROM products 
        WHERE listId = :listId 
        ORDER BY name COLLATE NOCASE ASC, position ASC
        """
    )
    fun observeByListOrderedByName(listId: Long): Flow<List<ProductEntity>>

    @Query(
        """
        SELECT * FROM products 
        WHERE listId = :listId 
        ORDER BY isPurchased ASC, position ASC
        """
    )
    fun observeByListOrderedByStatus(listId: Long): Flow<List<ProductEntity>>

    @Update
    suspend fun update(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun delete(productId: Long)

    @Query("DELETE FROM products WHERE listId = :listId")
    suspend fun deleteByList(listId: Long)

    @Query("UPDATE products SET isPurchased = :purchased WHERE id = :productId")
    suspend fun setPurchased(productId: Long, purchased: Boolean)

    @Query("UPDATE products SET isPurchased = 1 WHERE listId = :listId")
    suspend fun markAllPurchased(listId: Long)

    @Query("UPDATE products SET isPurchased = 0 WHERE listId = :listId")
    suspend fun clearPurchased(listId: Long)

    @Query("UPDATE products SET position = :newPosition WHERE id = :productId")
    suspend fun updatePosition(productId: Long, newPosition: Int)

    @Query("SELECT MAX(position) FROM products WHERE listId = :listId")
    suspend fun getMaxPosition(listId: Long): Int?
}
