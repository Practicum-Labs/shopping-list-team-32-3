package ru.practicum.shoppinglist.root.domain.api

interface OnboardingRepository {
    suspend fun getOnboardPassed(): Boolean
    suspend fun setOnboardPassed()
}
