package ru.practicum.shoppinglist

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.shoppinglist.core.di.coreModule
import ru.practicum.shoppinglist.feature.listdetail.di.listDetailModule
import ru.practicum.shoppinglist.feature.lists.di.listsModule
import ru.practicum.shoppinglist.feature.onboarding.di.onboardingModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                coreModule,
                listDetailModule,
                listsModule,
                onboardingModule
            )
        }
    }
}
