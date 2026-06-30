package ru.practicum.shoppinglist.root.di

import org.koin.dsl.module
import ru.practicum.shoppinglist.root.data.OnboardingRepositoryImpl
import ru.practicum.shoppinglist.root.domain.api.OnboardingRepository
import ru.practicum.shoppinglist.root.ui.RootViewModel

val rootModule = module {
    single<OnboardingRepository> {
        OnboardingRepositoryImpl(get())
    }
    factory { RootViewModel(get()) }
}
