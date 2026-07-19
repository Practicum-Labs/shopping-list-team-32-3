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

abstract class LoginViewModelBase(
    initial: LoginContract.State
) : MviViewModel<
    LoginContract.State,
    LoginContract.Intent,
    LoginContract.Effect
    > (initial)

@OptIn(SavedStateHandleSaveableApi::class)
class LoginViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
) : LoginViewModelBase(
    initial = LoginContract.State()
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

        setState {
            copy(email = email, password = password)
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
    }

    override fun onIntent(intent: LoginContract.Intent) {
        when (intent) {
            is LoginContract.Intent.ValidateEmail -> validateEmail()
            is LoginContract.Intent.ValidatePassword -> validatePassword()
            is LoginContract.Intent.Enter -> enter()
            is LoginContract.Intent.Register -> viewModelScope.launch {
                sendEffect(LoginContract.Effect.NavigateToRegistration)
            }
            is LoginContract.Intent.Recovery -> viewModelScope.launch {
                sendEffect(LoginContract.Effect.NavigateToRecovery)
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
        changeEnterAndTotalError()
    }
    private fun validatePassword() {
        val password = state.value.password.text.toString()
        if (password.isNotEmpty() && !password.isValidPassword().isLengthValid) {
            setState {
                copy(passwordErrorId = R.string.auth_password_supporting_long)
            }
        }
    }

    private fun changePassword() {
        val password = state.value.password.text.toString()
        if (state.value.passwordErrorId != null && (password.isEmpty() || password.isValidPassword().isLengthValid)) {
            setState {
                copy(passwordErrorId = null)
            }
        }
        changeEnterAndTotalError()
    }

    private fun changeEnterAndTotalError() {
        setState {
            copy(
                totalError = null,
                enterEnabled = state.value.email.text.toString().isValidEmail() &&
                    state.value.password.text.toString().isValidPassword().isLengthValid
            )
        }
    }

    private fun enter() {
        validateEmail()
        validatePassword()
        if (state.value.enterEnabled) {
            setState {
                copy(isLoading = true)
            }
            viewModelScope.launch {
                try {
                    authRepository.login(
                        state.value.email.text.toString(),
                        state.value.password.text.toString()
                    )
                    sendEffect(LoginContract.Effect.NavigateToLists)
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
}
