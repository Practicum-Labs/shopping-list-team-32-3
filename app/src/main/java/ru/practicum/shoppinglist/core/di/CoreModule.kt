package ru.practicum.shoppinglist.core.di

import androidx.room.Room
import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.core.data.preferences.PreferencesService

private const val DB_NAME = "shoppinglist.db"

val coreModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, DB_NAME)
            .build()
    }

    single {
        PreferencesService(get())
    }
}
