package ru.practicum.shoppinglist.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.room.Room
import com.google.crypto.tink.Aead
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.practicum.shoppinglist.core.data.database.AppDatabase
import ru.practicum.shoppinglist.core.data.network.SuccessCheckInterceptor
import ru.practicum.shoppinglist.core.data.preferences.AuthPreferences
import ru.practicum.shoppinglist.core.data.preferences.AuthStorage
import ru.practicum.shoppinglist.core.data.preferences.CryptoManager
import ru.practicum.shoppinglist.core.data.preferences.EncryptedAuthSerializer
import ru.practicum.shoppinglist.core.data.preferences.PreferencesService
import java.io.File

private const val DB_NAME = "shoppinglist.db"
private const val BASE_URL = "https://practicumopbackend-production.up.railway.app"

private const val KEYSET_NAME = "shoppinglist_datastore_keyset"
private const val KEYSET_FILE = "shoppinglist_datastore_crypto_prefs"
private const val KEYSET_MASTERKEY_URI = "android-keystore://shoppinglist_datastore_master_key"

private const val AUTH_FILE = "shoppinglist_datastore/auth_prefs.pb"

object CoreDiKeys {
    const val BASE_URL_KEY = "BASE_URL_KEY"
}

val coreModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, DB_NAME)
            .build()
    }

    single {
        PreferencesService(get())
    }

    single<OkHttpClient.Builder> {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(SuccessCheckInterceptor())
        httpClientBuilder
    }

    single<OkHttpClient> {
        get<OkHttpClient.Builder>().build()
    }

    single<Converter.Factory> {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val contentType = "application/json".toMediaType()
        json.asConverterFactory(contentType)
    }

    single(named(CoreDiKeys.BASE_URL_KEY)) {
        BASE_URL
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<String>(named(CoreDiKeys.BASE_URL_KEY)))
            .client(get())
            .addConverterFactory(get())
            .build()
    }

    single<Aead> {
        CryptoManager.getAead(get(), KEYSET_NAME, KEYSET_FILE, KEYSET_MASTERKEY_URI)
    }

    single<Serializer<AuthPreferences>> {
        EncryptedAuthSerializer(aead = get())
    }

    single<DataStore<AuthPreferences>> {
        DataStoreFactory.create(
            serializer = get(),
            produceFile = { File(get<Context>().filesDir, AUTH_FILE) }
        )
    }

    single {
        AuthStorage(dataStore = get())
    }
}
