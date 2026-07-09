package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
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
import ru.practicum.shoppinglist.feature.auth.ui.preview.RecoveryPreviewProvider

@Composable
fun RecoveryScreen(
    onBack: () -> Unit,
    viewModel: RecoveryViewModelBase = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RecoveryEffectsHandler(
        viewModel,
        onBack,
    )
    RecoveryScreenContent(
        state,
        viewModel::onIntent
    )
}

@Composable
private fun RecoveryEffectsHandler(
    viewModel: RecoveryViewModelBase,
    onNavigateToBack: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is RecoveryContract.Effect.NavigateToBack -> onNavigateToBack()
            }
        }
    }
}

@Composable
private fun RecoveryScreenContent(
    state: RecoveryContract.State,
    onIntent: (RecoveryContract.Intent) -> Unit,
) {
    val scrollState = rememberScrollState()
    FullScreenLoader(state.isLoading) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    stringResource(R.string.auth_recovery_screen_title),
                    onBack = { onIntent(RecoveryContract.Intent.Back) }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
                    .verticalScroll(scrollState)
                    .padding(horizontal = Dimens.padding24)
                    .padding(bottom = Dimens.padding24)

            ) {
                AuthGreeting(
                    R.drawable.image_auth_recovery,
                    R.string.auth_recovery_title,
                    R.string.auth_recovery_description,
                    modifier = Modifier.padding(Dimens.padding20)
                )

                RecoveryFormFields(
                    state = state,
                    onIntent = onIntent,
                )

                if (state.success) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    SuccessCard(state.email.text.toString())
                }
            }
        }
    }
}

@Suppress("CognitiveComplexMethod")
@Composable
private fun RecoveryFormFields(
    state: RecoveryContract.State,
    onIntent: (RecoveryContract.Intent) -> Unit,
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
                    onIntent(RecoveryContract.Intent.ValidateEmail)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { imeAction ->
                onIntent(RecoveryContract.Intent.ValidateEmail)
                imeAction()
            },
            errorTextId = state.emailErrorId,
            supportingTextId = R.string.auth_recovery_email_supporting
        )

        if (!state.totalError.isNullOrEmpty()) {
            Text(
                stringResource(
                    R.string.auth_recovery_server_error,
                    state.totalError ?: stringResource(R.string.core_exc_error_unknown)
                ),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        PrimaryButton(
            R.string.auth_recovery_recover_title,
            {
                onIntent(
                    RecoveryContract.Intent.Recover
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.recoveryEnabled
        )
    }
}

@Composable
private fun SuccessCard(email: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = Dimens.padding16)

    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding12),
            modifier = Modifier
                .padding(Dimens.padding16)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_auth_recovery_success_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.icon32),
            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(Dimens.padding6),
            ) {
                Text(
                    stringResource(R.string.auth_recovery_success_title),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    stringResource(R.string.auth_recovery_success_description, email),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@AppPreview
@Composable
fun RecoveryScreenPreview(
    @PreviewParameter(RecoveryPreviewProvider::class) model: RecoveryViewModelBase
) {
    AppTheme {
        RecoveryScreen({}, model)
    }
}
