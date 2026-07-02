package ru.practicum.shoppinglist.feature.auth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.practicum.shoppinglist.core.data.preferences.AuthPreferences
import ru.practicum.shoppinglist.core.data.preferences.EncryptedAuthSerializer
import ru.practicum.shoppinglist.feature.auth.data.AuthNetworkClient
import ru.practicum.shoppinglist.feature.auth.data.AuthStorage
import ru.practicum.shoppinglist.feature.auth.data.network.AuthApi
import ru.practicum.shoppinglist.feature.auth.data.network.RetrofitAuthApiNetworkClient
import ru.practicum.shoppinglist.feature.auth.data.repository.AuthRepositoryImpl
import ru.practicum.shoppinglist.feature.auth.domain.api.AuthRepository
import java.io.File

private const val AUTH_FILE = "shoppinglist_datastore/auth_prefs.pb"
val authModule = module {
    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
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

    single<AuthNetworkClient>{
        RetrofitAuthApiNetworkClient(get())
    }

    single<AuthRepository>{
        AuthRepositoryImpl(get(), get())
    }
}
