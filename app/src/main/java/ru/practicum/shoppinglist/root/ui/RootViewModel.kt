package ru.practicum.shoppinglist.root.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.core.mvi.UiEffect
import ru.practicum.shoppinglist.core.mvi.UiIntent
import ru.practicum.shoppinglist.core.mvi.UiState
import ru.practicum.shoppinglist.root.domain.api.OnboardingRepository

interface RootContract {

    data class State(
        val passedOnboarding: Boolean? = null
    ) : UiState

    sealed interface Intent : UiIntent

    sealed interface Effect : UiEffect
}

class RootViewModel(
    val repository: OnboardingRepository
) : MviViewModel<RootContract.State, RootContract.Intent, RootContract.Effect>(
    initial = RootContract.State()
) {
    init {
        viewModelScope.launch {
            val passedOnboarding = repository.getOnboardPassed()
            setState { copy(passedOnboarding = passedOnboarding) }
            repository.setOnboardPassed()
        }
    }
    override fun onIntent(intent: RootContract.Intent) {
    }
}
