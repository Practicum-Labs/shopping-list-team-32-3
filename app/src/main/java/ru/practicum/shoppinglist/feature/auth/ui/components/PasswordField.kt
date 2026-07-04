package ru.practicum.shoppinglist.feature.auth.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.components.AppTextField
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    @StringRes labelId: Int = R.string.auth_password_label,
    @StringRes placeholderId: Int = R.string.auth_password_label,
    @StringRes errorTextId: Int? = null,
    hiddenState: MutableState<Boolean> = remember { mutableStateOf(true) }
) {
    AppTextField(
        state,
        labelId,
        placeholderId,
        modifier,
        errorTextId = errorTextId,
        outputTransformation = if (hiddenState.value) passwordTransformation else null,
        trailingIcon = {
            IconButton(
                onClick = {
                    hiddenState.value = !hiddenState.value
                }
            ) {
                Icon(
                    painter = painterResource(
                        if (hiddenState.value) {
                            R.drawable.ic_core_opened_eye_icon
                        } else {
                            R.drawable.ic_core_closed_eye_icon
                        }
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(Dimens.icon20)
                )
            }
        }
    )
}

private val passwordTransformation = OutputTransformation {
    replace(0, length, "*".repeat(length))
}

@AppPreview
@Composable
private fun PasswordFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            PasswordField(
                state = rememberTextFieldState("123"),
                Modifier.padding(Dimens.padding16),
            )

            PasswordField(
                state = rememberTextFieldState("123"),
                Modifier.padding(Dimens.padding16),
                hiddenState = remember { mutableStateOf(false) }
            )

            PasswordField(
                state = rememberTextFieldState("123"),
                Modifier.padding(Dimens.padding16),
                hiddenState = remember { mutableStateOf(false) },
                errorTextId = R.string.auth_login_divider_label
            )
        }
    }
}
