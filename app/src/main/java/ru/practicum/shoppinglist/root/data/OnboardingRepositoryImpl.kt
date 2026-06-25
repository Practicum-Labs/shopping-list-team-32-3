package ru.practicum.shoppinglist.root.data

import ru.practicum.shoppinglist.core.data.preferences.PreferencesService
import ru.practicum.shoppinglist.root.domain.api.OnboardingRepository

class OnboardingRepositoryImpl(val preferencesService: PreferencesService) : OnboardingRepository {
    override suspend fun getOnboardPassed(): Boolean {
        return preferencesService.get<Boolean>(ONBOARD_KEY) == true
    }

    override suspend fun setOnboardPassed() {
        preferencesService.put(ONBOARD_KEY, true)
    }

    companion object {
        const val ONBOARD_KEY = "ONBOARD_KEY"
    }
}
