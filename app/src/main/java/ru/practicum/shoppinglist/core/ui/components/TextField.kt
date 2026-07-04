package ru.practicum.shoppinglist.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    AppTextField(state, labelId, placeholderId, modifier, keyboardOptions, onKeyboardAction)
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
    onFocusChange: ((Boolean) -> Unit)? = null

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
                color = MaterialTheme.colorScheme.secondary,
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
            .copy(cursorColor = MaterialTheme.colorScheme.secondary),
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        interactionSource = interactionSource
    )
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
                    Modifier.padding(Dimens.padding16)
                        .weight(1f)
                )
                AppTextField(
                    state = rememberTextFieldState("123"),
                    R.string.lists_add_label,
                    R.string.lists_add_placeholder,
                    Modifier.padding(Dimens.padding16)
                        .weight(1f)
                )

                AppTextField(
                    state = rememberTextFieldState("123"),
                    R.string.lists_add_label,
                    R.string.lists_add_placeholder,
                    Modifier.padding(Dimens.padding16)
                        .weight(1f)
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
        }
    }
}
