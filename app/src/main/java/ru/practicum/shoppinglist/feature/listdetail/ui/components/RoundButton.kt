package ru.practicum.shoppinglist.feature.listdetail.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun RoundButton(
    @DrawableRes iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceTint,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier
            .size(Dimens.icon48)
            .padding(Dimens.padding4)

    ) {
        Icon(
            painterResource(iconResId),
            contentDescription = null
        )
    }
}

@AppPreview
@Composable
fun RoundButtonPreview() {
    AppTheme {
        Row {
            RoundButton(R.drawable.ic_listdetail_minus_icon, {}, enabled = false)
            RoundButton(R.drawable.ic_listdetail_plus_icon, {})
        }
    }
}
