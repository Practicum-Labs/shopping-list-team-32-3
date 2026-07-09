package ru.practicum.shoppinglist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import ru.practicum.shoppinglist.core.di.coreModule
import ru.practicum.shoppinglist.feature.auth.di.authModule
import ru.practicum.shoppinglist.feature.listdetail.di.listDetailModule
import ru.practicum.shoppinglist.feature.lists.di.listsModule
import ru.practicum.shoppinglist.feature.onboarding.di.onboardingModule
import ru.practicum.shoppinglist.root.di.rootModule

var appModules: List<Module> = listOf(
    coreModule,
    rootModule,
    authModule,
    listDetailModule,
    listsModule,
    onboardingModule
)


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                appModules
            )
        }
    }
}
