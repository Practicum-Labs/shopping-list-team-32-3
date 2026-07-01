package ru.practicum.shoppinglist.feature.auth.di

import org.koin.dsl.module
import retrofit2.Retrofit
import ru.practicum.shoppinglist.feature.auth.data.repository.network.AuthApi

val authModule = module {
    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }
}
