package ru.practicum.shoppinglist.root.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.BuildConfig
import ru.practicum.shoppinglist.core.domain.api.AuthRepository
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.core.mvi.UiEffect
import ru.practicum.shoppinglist.core.mvi.UiIntent
import ru.practicum.shoppinglist.core.mvi.UiState
import ru.practicum.shoppinglist.root.domain.api.OnboardingRepository
import ru.practicum.shoppinglist.root.domain.api.ShakeRepository

enum class InitialState {
    ONBOARDING,
    AUTH,
    CONTENT
}
interface RootContract {

    data class State(
        val recompositionTrigger: Int = 0,
        val initialState: InitialState? = null,
        val showDebugMenu: Boolean = false
    ) : UiState

    sealed interface Intent : UiIntent {
        object CloseDebugMenu : Intent
        object FakeLogin : Intent
    }

    sealed interface Effect : UiEffect
}

class RootViewModel(
    val repository: OnboardingRepository,
    val authRepository: AuthRepository,
    val shakeRepository: ShakeRepository
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
        if (BuildConfig.DEBUG) {
            viewModelScope.launch {
                shakeRepository.shakeEvents().collect {
                    setState { copy(showDebugMenu = true) }
                }
            }
        }
    }
    override fun onIntent(intent: RootContract.Intent) {
        when (intent) {
            is RootContract.Intent.CloseDebugMenu -> closeDebugMenu()
            is RootContract.Intent.FakeLogin -> fakeLogin()
        }
    }

    private fun closeDebugMenu() {
        viewModelScope.launch {
            setState { copy(showDebugMenu = false) }
        }
    }

    private fun fakeLogin() {
        if (BuildConfig.DEBUG) {
            viewModelScope.launch {
                setState {
                    copy(
                        showDebugMenu = false,
                        initialState = null,
                        recompositionTrigger = recompositionTrigger + 1
                    )
                }
                viewModelScope.launch {
                    authRepository.fakeLogin()
                    setState {
                        copy(
                            initialState = InitialState.CONTENT,
                            recompositionTrigger = recompositionTrigger + 1
                        )
                    }
                }
            }
        }
    }
}
