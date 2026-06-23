package ru.practicum.shoppinglist.core.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class MviViewModel<S : UiState, I : UiIntent, E : UiEffect>(
    initial: S
) : ViewModel() {

    private val _state = MutableStateFlow(initial)
    val state: StateFlow<S> = _state

    private val _effects = MutableSharedFlow<E>()
    val effects: SharedFlow<E> = _effects

    abstract fun onIntent(intent: I)

    protected fun setState(reduce: S.() -> S) {
        _state.value = _state.value.reduce()
    }

    protected suspend fun sendEffect(effect: E) {
        _effects.emit(effect)
    }

    protected fun currentState(): S = _state.value
}