package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.AppTextField
import ru.practicum.shoppinglist.core.ui.components.DividerWithText
import ru.practicum.shoppinglist.core.ui.components.FullScreenLoader
import ru.practicum.shoppinglist.core.ui.components.PrimaryButton
import ru.practicum.shoppinglist.core.ui.components.SecondaryButton
import ru.practicum.shoppinglist.core.ui.components.TextButton
import ru.practicum.shoppinglist.core.ui.components.TopAppBar
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.auth.ui.components.AuthGreeting
import ru.practicum.shoppinglist.feature.auth.ui.components.PasswordField
import ru.practicum.shoppinglist.feature.auth.ui.preview.LoginPreviewProvider

@Composable
fun LoginScreen(
    onNavigateToRegistration: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onNavigateToLists: () -> Unit,
    viewModel: LoginViewModelBase = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginEffectsHandler(
        viewModel,
        onNavigateToRegistration,
        onNavigateToRecovery,
        onNavigateToLists
    )
    LoginScreenContent(
        state,
        viewModel::onIntent,
        onNavigateToLists
    )
}

@Composable
private fun LoginEffectsHandler(
    viewModel: LoginViewModelBase,
    onNavigateToRegistration: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onNavigateToLists: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is LoginContract.Effect.NavigateToRecovery -> onNavigateToRecovery()
                is LoginContract.Effect.NavigateToRegistration -> onNavigateToRegistration()
                is LoginContract.Effect.NavigateToLists -> onNavigateToLists()
            }
        }
    }
}

@Composable
private fun LoginScreenContent(
    state: LoginContract.State,
    onIntent: (LoginContract.Intent) -> Unit,
    onNavigateToLists: () -> Unit,
) {
    val scrollState = rememberScrollState()

    FullScreenLoader(state.isLoading) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = { TopAppBar(stringResource(R.string.auth_login_screen_title)) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(horizontal = Dimens.padding24)
            ) {
                AuthGreeting(
                    R.drawable.image_auth_login,
                    R.string.auth_login_title,
                    R.string.auth_login_description,
                    modifier = Modifier.padding(Dimens.padding20)
                )

                LoginFormFields(
                    state = state,
                    onIntent = onIntent,
                    onNavigateToLists = onNavigateToLists
                )
            }
        }
    }
}

@Composable
private fun LoginFormFields(
    state: LoginContract.State,
    onIntent: (LoginContract.Intent) -> Unit,
    onNavigateToLists: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.padding16),
    ) {
        AppTextField(
            state = state.email,
            labelId = R.string.auth_email_label,
            placeholderId = R.string.auth_email_label,
            modifier = Modifier.fillMaxWidth(),
            onFocusChange = { focused ->
                if (!focused) {
                    onIntent(LoginContract.Intent.ValidateEmail)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { imeAction ->
                onIntent(LoginContract.Intent.ValidateEmail)
                imeAction()
            },
            errorTextId = state.emailErrorId
        )
        PasswordField(
            state = state.password,
            modifier = Modifier.fillMaxWidth(),
            onFocusChange = { focused ->
                if (!focused) {
                    onIntent(LoginContract.Intent.ValidatePassword)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { imeAction ->
                onIntent(LoginContract.Intent.ValidatePassword)
                imeAction()
            },
            errorTextId = state.passwordErrorId
        )
        if (!state.totalError.isNullOrEmpty()) {
            Text(
                stringResource(
                    R.string.auth_login_server_error,
                    state.totalError ?: stringResource(R.string.core_exc_error_unknown)
                ),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        PrimaryButton(
            R.string.auth_login_enter_title,
            {
                onIntent(
                    LoginContract.Intent.Enter
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.enterEnabled
        )
        DividerWithText(R.string.auth_login_divider_label)
        SecondaryButton(
            R.string.auth_registration_title,
            onClick = {
                onIntent(
                    LoginContract.Intent.Register
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            R.string.auth_login_recovery_title,
            onClick = {
                onIntent(
                    LoginContract.Intent.Recovery
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        // TODO убрать после настройки экрана регистрации
        // и привязки списков к пользователю
        // Быстрый переход к спискам
        Button(
            onClick = onNavigateToLists,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("К спискам без логина!")
        }
    }
}

@AppPreview
@Composable
fun LoginScreenPreview(
    @PreviewParameter(LoginPreviewProvider::class) model: LoginViewModelBase
) {
    AppTheme {
        LoginScreen({}, {}, {}, model)
    }
}
