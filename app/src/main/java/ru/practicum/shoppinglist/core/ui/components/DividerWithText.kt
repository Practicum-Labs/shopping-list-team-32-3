package ru.practicum.shoppinglist.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun DividerWithText(
    @StringRes textId: Int,
    color:  Color = MaterialTheme.colorScheme.onSurface,
    background: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalDivider(
            thickness = Dimens.border1,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier
                .background(background)
                .padding(horizontal = Dimens.padding16),

        )
    }
}

@AppPreview
@Composable
private fun DividerWithTextPreview(){
    AppTheme {
        DividerWithText(R.string.auth_login_enter_title)
    }
}