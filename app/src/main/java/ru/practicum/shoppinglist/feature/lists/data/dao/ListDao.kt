package ru.practicum.shoppinglist.feature.lists.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity

@Dao
interface ListDao {
    @Query("SELECT * FROM lists ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<ListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(list: ListEntity): Long

    @Query("UPDATE lists SET name = :newName WHERE id = :listId")
    suspend fun rename(listId: Long, newName: String)

    @Query("DELETE FROM lists WHERE id = :listId")
    suspend fun deleteById(listId: Long)

    @Query("DELETE FROM lists")
    suspend fun deleteAll()
}
