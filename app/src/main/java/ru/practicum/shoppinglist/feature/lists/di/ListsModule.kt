package ru.practicum.shoppinglist.feature.lists.di

import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase

val listsModule = module {
    single { get<AppDatabase>().listDao() }
}
