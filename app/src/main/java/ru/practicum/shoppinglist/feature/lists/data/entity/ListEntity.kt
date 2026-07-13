package ru.practicum.shoppinglist.feature.lists.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val iconKey: String = "list_alt",
    val sortMode: String = "manual",
    val createdAt: Long = System.currentTimeMillis()
)
