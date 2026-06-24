package ru.practicum.shoppinglist.feature.lists.di

import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.feature.lists.data.dao.ListDao
import ru.practicum.shoppinglist.feature.lists.data.repository.ListsRepositoryImpl
import ru.practicum.shoppinglist.feature.lists.domain.api.ListsRepository

val listsModule = module {
    single<ListDao> { get<AppDatabase>().listDao() }
    single<ListsRepository> { ListsRepositoryImpl(get()) }
}
