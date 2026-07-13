package ru.practicum.shoppinglist.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    @StringRes placeholderId: Int,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    @StringRes errorTextId: Int? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val state = rememberTextFieldState(value)
    LaunchedEffect(state) {
        snapshotFlow { state.text }
            .distinctUntilChanged()
            .collect {
                onValueChange(it.toString())
            }
    }
    LaunchedEffect(value) {
        if (state.text.toString() != value) state.setTextAndPlaceCursorAtEnd(value)
    }
    AppTextField(
        state,
        labelId,
        placeholderId,
        modifier,
        keyboardOptions,
        onKeyboardAction,
        errorTextId = errorTextId,
        trailingIcon = trailingIcon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    state: TextFieldState,
    @StringRes labelId: Int,
    @StringRes placeholderId: Int,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    onFocusChange: ((Boolean) -> Unit)? = null,
    @StringRes errorTextId: Int? = null,
    outputTransformation: OutputTransformation? = null,
    @StringRes supportingTextId: Int? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    LaunchedEffect(isFocused) {
        onFocusChange?.invoke(isFocused)
    }

    OutlinedTextField(
        state = state,
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        label = {
            Text(
                stringResource(labelId),
                style = MaterialTheme.typography.bodySmall,
                // color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = Dimens.padding4),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        placeholder = {
            Text(
                stringResource(placeholderId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors()
            .copy(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.secondary,
                focusedLabelColor = MaterialTheme.colorScheme.secondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
                errorSupportingTextColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
            ),
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        interactionSource = interactionSource,
        isError = errorTextId != null,
        supportingText = {
            if (errorTextId != null) ErrorText(errorTextId) else SupportingText(supportingTextId)
        },
        outputTransformation = outputTransformation
    )
}

@Composable
private fun ErrorText(@StringRes value: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.padding4),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.offset(x = -Dimens.padding16),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_core_warning_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
        Text(
            stringResource(value),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun SupportingText(@StringRes value: Int?) {
    value?.let {
        Text(
            stringResource(value),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.offset(x = -Dimens.padding16),
        )
    }
}

@AppPreview
@Composable
private fun AppTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Row {
                AppTextField(
                    value = "",
                    {},
                    R.string.lists_add_label,
                    R.string.lists_add_placeholder,
                    Modifier
                        .padding(Dimens.padding16)
                        .weight(1f)
                )
                AppTextField(
                    state = rememberTextFieldState("123"),
                    R.string.lists_add_label,
                    R.string.lists_add_placeholder,
                    Modifier
                        .padding(Dimens.padding16)
                        .weight(1f)
                )

                AppTextField(
                    state = rememberTextFieldState("123"),
                    R.string.lists_add_label,
                    R.string.lists_add_placeholder,
                    Modifier
                        .padding(Dimens.padding16)
                        .weight(1f),
                    errorTextId = R.string.auth_login_recovery_title
                )
            }
            AppTextField(
                value = "",
                {},
                R.string.lists_add_label,
                R.string.lists_add_placeholder,
                Modifier.padding(Dimens.padding16)
            )
            AppTextField(
                state = rememberTextFieldState("123"),
                R.string.lists_add_label,
                R.string.lists_add_placeholder,
                Modifier.padding(Dimens.padding16)
            )
            AppTextField(
                state = rememberTextFieldState("123"),
                R.string.lists_add_label,
                R.string.lists_add_placeholder,
                Modifier.padding(Dimens.padding16),
                errorTextId = R.string.auth_login_recovery_title
            )
        }
    }
}
