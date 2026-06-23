package ru.practicum.shoppinglist.feature.listdetail.domain.models

// Шаблон имени товара
// Ранее введённое имя товара, сохранённое для автоподсказок.
data class NameTemplate(
    val id: Long,
    val name: String
)

typealias NameTemplates = List<NameTemplate>
