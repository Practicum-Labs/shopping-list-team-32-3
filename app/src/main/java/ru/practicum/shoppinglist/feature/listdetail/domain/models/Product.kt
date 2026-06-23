package ru.practicum.shoppinglist.feature.listdetail.domain.models

data class Product(
    val id: Long,
    val listId: Long,
    val name: String,
    val quantity: Double?,
    val unit: Unit?,
    // Купленный товар
    // Товар, отмеченный пользователем как приобретённый.
    // В UI отображается зачёркиванием.
    val isPurchased: Boolean,
    val position: Int
)

typealias Products = List<Product>
