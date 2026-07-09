package ru.practicum.shoppinglist.feature.auth.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.practicum.shoppinglist.core.data.repository.AuthRepositoryImpl
import ru.practicum.shoppinglist.core.domain.api.AuthRepository
import ru.practicum.shoppinglist.feature.auth.data.AuthNetworkClient
import ru.practicum.shoppinglist.feature.auth.data.network.AuthApi
import ru.practicum.shoppinglist.feature.auth.data.network.RetrofitAuthApiNetworkClient
import ru.practicum.shoppinglist.feature.auth.ui.LoginViewModel
import ru.practicum.shoppinglist.feature.auth.ui.LoginViewModelBase
import ru.practicum.shoppinglist.feature.auth.ui.RecoveryViewModel
import ru.practicum.shoppinglist.feature.auth.ui.RecoveryViewModelBase
import ru.practicum.shoppinglist.feature.auth.ui.RegisterViewModel
import ru.practicum.shoppinglist.feature.auth.ui.RegisterViewModelBase

val authModule = module {
    single<AuthApi> {
        get<Retrofit>().create(AuthApi::class.java)
    }

    single<AuthNetworkClient> {
        RetrofitAuthApiNetworkClient(get())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }

    viewModel<LoginViewModelBase> {
        LoginViewModel(get(), get())
    }

    factory<RecoveryViewModelBase> {
        RecoveryViewModel(get(), get())
    }

    factory<RegisterViewModelBase> {
        RegisterViewModel(get(), get())
    }
}
