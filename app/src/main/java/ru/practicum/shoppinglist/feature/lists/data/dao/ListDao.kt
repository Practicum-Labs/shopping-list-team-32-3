package ru.practicum.shoppinglist.feature.lists.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity

@Dao
interface ListDao {
    @Query("SELECT * FROM lists WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeAll(userId: Long): Flow<List<ListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(list: ListEntity): Long

    @Query("UPDATE lists SET name = :newName WHERE id = :listId")
    suspend fun rename(listId: Long, newName: String)

    @Query("DELETE FROM lists WHERE id = :listId")
    suspend fun deleteById(listId: Long)

    @Query("DELETE FROM lists WHERE userId = :userId")
    suspend fun deleteAll(userId: Long)

    @Query("SELECT * FROM lists WHERE id = :listId LIMIT 1")
    suspend fun getListById(listId: Long): ListEntity?

    @Query("UPDATE lists SET sortMode = :sortMode WHERE id = :listId")
    suspend fun updateSortMode(listId: Long, sortMode: String)

    @Query("UPDATE lists SET iconKey = :newKey WHERE id = :listId")
    suspend fun changeIcon(listId: Long, newKey: String)
}
