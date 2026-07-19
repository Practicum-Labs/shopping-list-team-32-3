package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.domain.api.AuthRepository
import ru.practicum.shoppinglist.core.domain.exception.DataException
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.feature.auth.domain.extentions.isValidEmail
import ru.practicum.shoppinglist.feature.auth.domain.extentions.isValidPassword
import ru.practicum.shoppinglist.feature.auth.domain.models.PasswordValidation

abstract class RegisterViewModelBase(
    initial: RegisterContract.State
) : MviViewModel<
    RegisterContract.State,
    RegisterContract.Intent,
    RegisterContract.Effect
    > (initial)

@OptIn(SavedStateHandleSaveableApi::class)
class RegisterViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
) : RegisterViewModelBase(
    initial = RegisterContract.State()
) {
    init {
        val email: TextFieldState by savedStateHandle.saveable(
            saver = TextFieldState.Saver
        ) {
            TextFieldState(initialText = "")
        }

        val password: TextFieldState by savedStateHandle.saveable(
            saver = TextFieldState.Saver
        ) {
            TextFieldState(initialText = "")
        }

        val repeat: TextFieldState by savedStateHandle.saveable(
            saver = TextFieldState.Saver
        ) {
            TextFieldState(initialText = "")
        }

        setState {
            copy(email = email, password = password, repeat = repeat)
        }

        viewModelScope.launch {
            snapshotFlow { email.text.toString() }
                .collectLatest {
                    changeEmail()
                }
        }
        viewModelScope.launch {
            snapshotFlow { password.text.toString() }
                .collectLatest {
                    changePassword()
                }
        }
        viewModelScope.launch {
            snapshotFlow { repeat.text.toString() }
                .collectLatest {
                    changeRepeat()
                }
        }
    }

    override fun onIntent(intent: RegisterContract.Intent) {
        when (intent) {
            is RegisterContract.Intent.ValidateEmail -> validateEmail()
            is RegisterContract.Intent.ValidatePassword -> validatePassword()
            is RegisterContract.Intent.ValidateRepeat -> validateRepeat()
            is RegisterContract.Intent.Register -> register()
            is RegisterContract.Intent.Back -> viewModelScope.launch {
                sendEffect(RegisterContract.Effect.NavigateToBack)
            }
        }
    }

    private fun validateEmail() {
        val email = state.value.email.text.toString()
        if (email.isNotEmpty() && !email.isValidEmail()) {
            setState {
                copy(emailErrorId = R.string.auth_email_supporting_label)
            }
        }
    }

    private fun changeEmail() {
        val email = state.value.email.text.toString()
        if (state.value.emailErrorId != null && (email.isEmpty() || email.isValidEmail())) {
            setState {
                copy(emailErrorId = null)
            }
        }
        changeRegisterAndTotalError()
    }
    private fun validatePassword() {
        val password = state.value.password.text.toString()
        val validity = password.isValidPassword()
        if (password.isNotEmpty() && !validity.isValid) {
            setState {
                copy(
                    passwordErrorId = getValidityString(validity)
                )
            }
        }
        validateRepeat()
    }

    private fun changePassword() {
        val password = state.value.password.text.toString()
        setState {
            copy(strength = password.isValidPassword().getStrength())
        }
        if (state.value.passwordErrorId != null) {
            val validity = password.isValidPassword()

            if (password.isEmpty() || validity.isValid) {
                setState {
                    copy(passwordErrorId = null)
                }
            } else {
                setState {
                    copy(passwordErrorId = getValidityString(validity))
                }
            }
        }
        changeRegisterAndTotalError()
    }

    private fun validateRepeat() {
        val repeat = state.value.repeat.text.toString()
        if (repeat.isNotEmpty() && repeat != state.value.password.text.toString()) {
            setState {
                copy(repeatErrorId = R.string.auth_register_repeat_error)
            }
        }
    }

    private fun changeRepeat() {
        val repeat = state.value.repeat.text.toString()
        if (state.value.repeatErrorId != null && (repeat.isEmpty() || repeat == state.value.password.text.toString())) {
            setState {
                copy(repeatErrorId = null)
            }
        }
        changeRegisterAndTotalError()
    }

    private fun changeRegisterAndTotalError() {
        setState {
            copy(
                totalError = null,
                registerEnabled = state.value.email.text.toString().isValidEmail() &&
                    state.value.password.text.toString().isValidPassword().isValid &&
                    state.value.repeat.text.toString() == state.value.password.text.toString()
            )
        }
    }

    private fun register() {
        validateEmail()
        validatePassword()
        validateRepeat()
        if (state.value.registerEnabled) {
            setState {
                copy(isLoading = true)
            }
            viewModelScope.launch {
                try {
                    authRepository.register(
                        state.value.email.text.toString(),
                        state.value.password.text.toString()
                    )
                    sendEffect(RegisterContract.Effect.NavigateToLists)
                } catch (e: DataException.Network) {
                    setState {
                        copy(
                            isLoading = false,
                            totalError = e.message,
                        )
                    }
                }
            }
        }
    }

    private fun getValidityString(validity: PasswordValidation): Int? {
        return when {
            !validity.isLengthValid -> R.string.auth_password_supporting_long
            !validity.hasDigit -> R.string.auth_register_password_supporting_digit
            !validity.hasUppercase || !validity.hasLowercase -> R.string.auth_register_password_supporting_case
            !validity.hasSpecialChar -> R.string.auth_register_password_supporting_special
            else -> null
        }
    }
}
