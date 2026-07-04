package ru.practicum.shoppinglist.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun ConfirmDialog(
    @StringRes titleId: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmDialog(stringResource(titleId), onConfirm, onDismiss)
}

@Composable
fun ConfirmDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_core_confirm_dialog_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.icon24)
            )
        },

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        shape = RoundedCornerShape(Dimens.radius28),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        confirmButton = {
            PrimaryButton(
                R.string.core_confirm_dialog_delete,
                onConfirm
            )
        },
        dismissButton = {
            SecondaryButton(
                R.string.core_confirm_dialog_cancel,
                onConfirm
            )
        }
    )
}

@AppPreview
@Composable
private fun ConfirmDialogPreview() {
    AppTheme {
        ConfirmDialog(
            R.string.lists_alert_delete_title,
            {},
            {}
        )
    }
}
