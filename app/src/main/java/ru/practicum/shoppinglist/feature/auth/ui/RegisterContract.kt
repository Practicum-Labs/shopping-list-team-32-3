package ru.practicum.shoppinglist.feature.auth.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.text.input.TextFieldState
import ru.practicum.shoppinglist.core.mvi.UiEffect
import ru.practicum.shoppinglist.core.mvi.UiIntent
import ru.practicum.shoppinglist.core.mvi.UiState
import ru.practicum.shoppinglist.feature.auth.domain.models.PasswordStrength

interface RegisterContract {
    data class State(
        val isLoading: Boolean = false,
        val email: TextFieldState = TextFieldState(""),
        val password: TextFieldState = TextFieldState(""),
        val strength: PasswordStrength = PasswordStrength.NONE,
        val repeat: TextFieldState = TextFieldState(""),
        @StringRes val emailErrorId: Int? = null,
        @StringRes val passwordErrorId: Int? = null,
        @StringRes val repeatErrorId: Int? = null,
        val totalError: String? = null,
        val registerEnabled: Boolean = false
    ) : UiState

    sealed interface Sheet

    sealed interface Intent : UiIntent {
        object ValidateEmail : Intent
        object ValidatePassword : Intent
        object ValidateRepeat : Intent
        object Register : Intent
        object Back : Intent
    }

    sealed interface Effect : UiEffect {
        object NavigateToBack : Effect
        object NavigateToLists : Effect
    }
}
