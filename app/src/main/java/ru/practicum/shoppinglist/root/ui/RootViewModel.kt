package ru.practicum.shoppinglist.root.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.core.mvi.UiEffect
import ru.practicum.shoppinglist.core.mvi.UiIntent
import ru.practicum.shoppinglist.core.mvi.UiState
import ru.practicum.shoppinglist.feature.auth.domain.api.AuthRepository
import ru.practicum.shoppinglist.root.domain.api.OnboardingRepository

enum class InitialState {
    ONBOARDING,
    AUTH,
    CONTENT
}
interface RootContract {

    data class State(
        val initialState: InitialState? = null
    ) : UiState

    sealed interface Intent : UiIntent

    sealed interface Effect : UiEffect
}

class RootViewModel(
    val repository: OnboardingRepository,
    val authRepository: AuthRepository
) : MviViewModel<RootContract.State, RootContract.Intent, RootContract.Effect>(
    initial = RootContract.State()
) {
    init {
        viewModelScope.launch {
            val passedOnboarding = repository.getOnboardPassed()
            repository.setOnboardPassed()
            if (passedOnboarding && authRepository.check()) {
                setState { copy(initialState = InitialState.CONTENT) }
            } else if (passedOnboarding) {
                setState { copy(initialState = InitialState.AUTH) }
            } else {
                setState { copy(initialState = InitialState.ONBOARDING) }
            }
        }
    }
    override fun onIntent(intent: RootContract.Intent) {
    }
}
