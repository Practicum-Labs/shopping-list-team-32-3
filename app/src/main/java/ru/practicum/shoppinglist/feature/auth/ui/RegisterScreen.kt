package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.AppTextField
import ru.practicum.shoppinglist.core.ui.components.FullScreenLoader
import ru.practicum.shoppinglist.core.ui.components.PrimaryButton
import ru.practicum.shoppinglist.core.ui.components.TopAppBar
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.auth.ui.components.AuthGreeting
import ru.practicum.shoppinglist.feature.auth.ui.components.PasswordField
import ru.practicum.shoppinglist.feature.auth.ui.components.StrengthLine
import ru.practicum.shoppinglist.feature.auth.ui.preview.RegisterPreviewProvider

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onNavigateToLists: () -> Unit,
    viewModel: RegisterViewModelBase = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterEffectsHandler(
        viewModel,
        onBack,
        onNavigateToLists
    )
    RegisterScreenContent(
        state,
        viewModel::onIntent
    )
}

@Composable
private fun RegisterEffectsHandler(
    viewModel: RegisterViewModelBase,
    onNavigateToBack: () -> Unit,
    onNavigateToLists: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is RegisterContract.Effect.NavigateToBack -> onNavigateToBack()
                is RegisterContract.Effect.NavigateToLists -> onNavigateToLists()
            }
        }
    }
}

@Composable
private fun RegisterScreenContent(
    state: RegisterContract.State,
    onIntent: (RegisterContract.Intent) -> Unit,
) {
    val scrollState = rememberScrollState()
    FullScreenLoader(state.isLoading) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    stringResource(R.string.auth_register_screen_title),
                    onBack = { onIntent(RegisterContract.Intent.Back) }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(horizontal = Dimens.padding24)
            ) {
                AuthGreeting(
                    R.drawable.image_auth_register,
                    R.string.auth_register_title,
                    R.string.auth_register_description,
                    modifier = Modifier.padding(Dimens.padding20)
                )

                RegisterFormFields(
                    state = state,
                    onIntent = onIntent
                )
            }
        }
    }
}

@Suppress("CognitiveComplexMethod")
@Composable
private fun RegisterFormFields(
    state: RegisterContract.State,
    onIntent: (RegisterContract.Intent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.padding16)
    ) {
        AppTextField(
            state = state.email,
            labelId = R.string.auth_email_label,
            placeholderId = R.string.auth_email_label,
            modifier = Modifier.fillMaxWidth(),
            onFocusChange = { focused ->
                if (!focused) {
                    onIntent(RegisterContract.Intent.ValidateEmail)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { imeAction ->
                onIntent(RegisterContract.Intent.ValidateEmail)
                imeAction()
            },
            errorTextId = state.emailErrorId
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            PasswordField(
                state = state.password,
                modifier = Modifier.fillMaxWidth(),
                onFocusChange = { focused ->
                    if (!focused) {
                        onIntent(RegisterContract.Intent.ValidatePassword)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                onKeyboardAction = { imeAction ->
                    onIntent(RegisterContract.Intent.ValidatePassword)
                    imeAction()
                },
                errorTextId = state.passwordErrorId
            )
            if (state.passwordErrorId == null) {
                StrengthLine(state.strength)
            }
        }
        PasswordField(
            labelId = R.string.auth_register_repeat_label,
            placeholderId = R.string.auth_register_repeat_label,
            state = state.repeat,
            modifier = Modifier.fillMaxWidth(),
            onFocusChange = { focused ->
                if (!focused) {
                    onIntent(RegisterContract.Intent.ValidateRepeat)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { imeAction ->
                onIntent(RegisterContract.Intent.ValidateRepeat)
                imeAction()
            },
            errorTextId = state.repeatErrorId
        )
        if (!state.totalError.isNullOrEmpty()) {
            Text(
                stringResource(
                    R.string.auth_register_server_error,
                    state.totalError ?: stringResource(R.string.core_exc_error_unknown)
                ),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        PrimaryButton(
            R.string.auth_register_title,
            {
                onIntent(
                    RegisterContract.Intent.Register
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.registerEnabled
        )
    }
}

@AppPreview
@Composable
fun RegisterScreenPreview(
    @PreviewParameter(RegisterPreviewProvider::class) model: RegisterViewModelBase
) {
    AppTheme {
        RegisterScreen({}, {}, model)
    }
}
