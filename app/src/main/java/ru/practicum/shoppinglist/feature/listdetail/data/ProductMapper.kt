package ru.practicum.shoppinglist.feature.listdetail.data

import ru.practicum.shoppinglist.feature.listdetail.data.entity.ProductEntity
import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit

fun ProductEntity.toDomain(): Product = Product(
    id = id,
    listId = listId,
    name = name,
    quantity = quantity,
    unit = unit?.let { ProductUnit.valueOf(it) },
    isPurchased = isPurchased,
    position = position
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    listId = listId,
    name = name,
    quantity = quantity,
    unit = unit?.name,
    isPurchased = isPurchased,
    position = position
)

fun List<ProductEntity>.toDomainList(): List<Product> = map { it.toDomain() }
fun List<Product>.toEntityList(): List<ProductEntity> = map { it.toEntity() }
