package ru.practicum.shoppinglist.feature.lists.ui

import ru.practicum.shoppinglist.feature.listdetail.ui.ListDetailViewModelBase

interface DetailViewModelFactory {
    fun create(id: Long): ListDetailViewModelBase
}
