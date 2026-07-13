package ru.practicum.shoppinglist.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.theme.AppTheme

@Composable
fun PrimaryButton(
    @StringRes titleId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    PrimaryButton(stringResource(titleId), onClick, modifier, enabled)
}

@Composable
fun PrimaryButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun SecondaryButton(
    @StringRes titleId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,

        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            stringResource(titleId),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun TextButton(
    @StringRes titleId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,

        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.outlineVariant,
        ),
        enabled = enabled,
        modifier = modifier
    ) {
        Text(
            stringResource(titleId),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@AppPreview
@Composable
private fun AppButtonsPreview() {
    AppTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PrimaryButton(R.string.core_confirm_dialog_delete, {})
            PrimaryButton(R.string.core_confirm_dialog_delete, {}, enabled = false)
            SecondaryButton(R.string.core_confirm_dialog_delete, {})
            SecondaryButton(R.string.core_confirm_dialog_delete, {}, enabled = false)
            TextButton(R.string.core_confirm_dialog_delete, {})
            TextButton(R.string.core_confirm_dialog_delete, {}, enabled = false)
        }
    }
}
