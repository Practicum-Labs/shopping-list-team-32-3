package ru.practicum.shoppinglist.feature.lists.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import org.koin.dsl.module
import ru.practicum.shoppinglist.core.data.database.AppDatabase

private const val DB_NAME = "shoppinglist.db"

val listsModule = module {
    single { get<AppDatabase>().listDao() }
}