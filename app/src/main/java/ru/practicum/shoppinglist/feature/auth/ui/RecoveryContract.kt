package ru.practicum.shoppinglist.feature.auth.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.text.input.TextFieldState
import ru.practicum.shoppinglist.core.mvi.UiEffect
import ru.practicum.shoppinglist.core.mvi.UiIntent
import ru.practicum.shoppinglist.core.mvi.UiState
import ru.practicum.shoppinglist.feature.auth.domain.models.PasswordStrength

interface RecoveryContract {
    data class State(
        val isLoading: Boolean = false,
        val email: TextFieldState = TextFieldState(""),
        @StringRes val emailErrorId: Int? = null,
        val totalError: String? = null,
        val recoveryEnabled: Boolean = true,
        val success: Boolean = false
    ) : UiState

    sealed interface Sheet

    sealed interface Intent : UiIntent {
        object ValidateEmail : Intent
        object Recover : Intent
        object Back : Intent
    }

    sealed interface Effect : UiEffect {
        object NavigateToBack : Effect
    }
}
