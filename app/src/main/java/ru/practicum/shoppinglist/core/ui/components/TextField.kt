package ru.practicum.shoppinglist.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    @StringRes  placeholderId: Int,
    modifier: Modifier = Modifier,

) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                    Text(
                        stringResource(labelId),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = Dimens.padding4)
                    )
            },
            placeholder = {
                Text(
                    stringResource(placeholderId),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            modifier = Modifier.padding(top = Dimens.padding8),
            colors = OutlinedTextFieldDefaults.colors()
                .copy(cursorColor = MaterialTheme.colorScheme.secondary)
        )

}

@AppPreview
@Composable
private fun AppTextFieldPreview() {
    AppTheme {
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            AppTextField(
                value = "",
                {},
                R.string.lists_add_label,
                R.string.lists_add_placeholder,
                Modifier.padding(Dimens.padding16)
            )
            AppTextField(
                value = "123",
                {},
                R.string.lists_add_label,
                R.string.lists_add_placeholder,
                Modifier.padding(Dimens.padding16)
            )
        }
    }
}