package ru.practicum.shoppinglist.feature.lists.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.repository.ListsRepositoryImpl
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository
import ru.practicum.shoppinglist.feature.lists.ui.ListsViewModel

val listsModule = module {
    single<ListDao> { get<AppDatabase>().listDao() }
    single<ListsRepository> { ListsRepositoryImpl(get()) }
    viewModelOf(::ListsViewModel)
}
