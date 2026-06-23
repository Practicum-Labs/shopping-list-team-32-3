package ru.practicum.shoppinglist.core.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase

private const val DB_NAME = "shoppinglist.db"

val coreModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, DB_NAME)
            .build()
    }
}