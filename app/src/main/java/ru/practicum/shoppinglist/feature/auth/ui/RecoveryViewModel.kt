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
import ru.practicum.shoppinglist.core.domain.exception.DataException
import ru.practicum.shoppinglist.core.mvi.MviViewModel
import ru.practicum.shoppinglist.feature.auth.domain.api.AuthRepository
import ru.practicum.shoppinglist.feature.auth.domain.extentions.isValidEmail
import ru.practicum.shoppinglist.feature.auth.domain.extentions.isValidPassword
import ru.practicum.shoppinglist.feature.auth.domain.models.PasswordValidation

abstract class RecoveryViewModelBase(
    initial: RecoveryContract.State
) : MviViewModel<
    RecoveryContract.State,
    RecoveryContract.Intent,
    RecoveryContract.Effect
    > (initial)

@OptIn(SavedStateHandleSaveableApi::class)
class RecoveryViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
) : RecoveryViewModelBase(
    initial = RecoveryContract.State()
) {
    init {
        val email: TextFieldState by savedStateHandle.saveable(
            saver = TextFieldState.Saver
        ) {
            TextFieldState(initialText = "")
        }

        setState {
            copy(email = email)
        }

        viewModelScope.launch {
            snapshotFlow { email.text.toString() }
                .collectLatest {
                    changeEmail()
                }
        }
    }

    override fun onIntent(intent: RecoveryContract.Intent) {
        when (intent) {
            is RecoveryContract.Intent.ValidateEmail -> validateEmail()
            is RecoveryContract.Intent.Recover -> recover()
            is RecoveryContract.Intent.Back -> viewModelScope.launch {
                sendEffect(RecoveryContract.Effect.NavigateToBack)
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
        changeRecoveryAndTotalError()
        setState {
            copy(success = false)
        }
    }


    private fun changeRecoveryAndTotalError() {
        setState {
            copy(
                totalError = null,
                recoveryEnabled = state.value.email.text.toString().isValidEmail()
            )
        }
    }

    private fun recover() {
        validateEmail()
        if (state.value.recoveryEnabled) {
            setState {
                copy(isLoading = true)
            }
            viewModelScope.launch {
                try {
                    authRepository.recovery(
                        state.value.email.text.toString()
                    )
                    setState {
                        copy(
                            isLoading = false,
                            success = true,
                        )
                    }
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
