package ru.practicum.shoppinglist.feature.listdetail.ui.preview

import ru.practicum.shoppinglist.feature.listdetail.domain.models.Product
import ru.practicum.shoppinglist.feature.listdetail.domain.models.ProductUnit

fun Product.Companion.mock1(): Product {
    return Product(1, 2, "Product 1", 0.5, ProductUnit.L, false, 1)
}

fun Product.Companion.mock2(): Product {
    return Product(2, 2, "Product 2", 0.5, ProductUnit.KG, true, 0)
}
