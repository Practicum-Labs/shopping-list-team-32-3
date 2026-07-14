package ru.practicum.shoppinglist.feature.listdetail.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppModalBottomSheet
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

@Composable
fun ListMenuSheet(
    currentSortMode: SortMode,
    hasPurchasedItems: Boolean,
    onSortClick: () -> Unit,
    onDeleteAllClick: () -> Unit,
    onClearPurchasedClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AppModalBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.padding8)
        ) {
            val sortSubtitle = when (currentSortMode) {
                SortMode.ALPHABETICAL -> stringResource(R.string.sort_mode_alphabetical)
                SortMode.MANUAL -> stringResource(R.string.sort_mode_manual)
            }

            MenuRow(
                title = stringResource(R.string.action_sort),
                subtitle = sortSubtitle,
                icon = R.drawable.ic_list_sort,
                onClick = {
                    onSortClick()
                    onDismiss()
                }
            )

            MenuRow(
                title = stringResource(R.string.action_delete_all_items),
                icon = R.drawable.ic_list_trashcan,
                onClick = {
                    onDeleteAllClick()
                    onDismiss()
                }
            )

            MenuRow(
                title = stringResource(R.string.action_clear_purchased),
                icon = R.drawable.ic_list_clear,
                enabled = hasPurchasedItems,
                onClick = {
                    onClearPurchasedClick()
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun MenuRow(
    title: String,
    icon: Int,
    onClick: () -> Unit,
    subtitle: String? = null,
    enabled: Boolean = true
) {
    val alpha = if (enabled) 1f else 0.38f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = Dimens.padding16, vertical = Dimens.padding16)
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(Dimens.padding16))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
