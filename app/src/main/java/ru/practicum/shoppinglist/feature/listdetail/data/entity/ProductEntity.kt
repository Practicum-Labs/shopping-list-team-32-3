package ru.practicum.shoppinglist.feature.listdetail.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.practicum.shoppinglist.feature.lists.data.entity.ListEntity

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["listId"], name = "idx_products_list_id")
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val listId: Long,
    val name: String,
    val quantity: Double? = null,
    val unit: String? = null,
    val isPurchased: Boolean = false,
    val position: Int = 0
)
