package ru.practicum.shoppinglist.core.di

import androidx.room.Room
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.core.data.network.SuccessCheckInterceptor
import ru.practicum.shoppinglist.core.data.preferences.PreferencesService

private const val DB_NAME = "shoppinglist.db"
private const val BASE_URL = "https://practicumopbackend-production.up.railway.app"

val coreModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, DB_NAME)
            .build()
    }

    single {
        PreferencesService(get())
    }

    single<Retrofit> {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(SuccessCheckInterceptor())
        val client: OkHttpClient = httpClientBuilder.build()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
