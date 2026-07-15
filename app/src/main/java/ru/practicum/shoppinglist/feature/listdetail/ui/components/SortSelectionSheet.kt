package ru.practicum.shoppinglist.feature.listdetail.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.AppModalBottomSheet
import ru.practicum.shoppinglist.core.ui.theme.Dimens
import ru.practicum.shoppinglist.feature.lists.domain.models.SortMode

@Composable
fun SortSelectionSheet(
    currentSortMode: SortMode,
    onSortModeSelected: (SortMode) -> Unit,
    onDismiss: () -> Unit
) {
    AppModalBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.padding8)
        ) {
            SortOptionRow(
                title = stringResource(R.string.sort_mode_alphabetical),
                isSelected = currentSortMode == SortMode.ALPHABETICAL,
                onClick = {
                    onSortModeSelected(SortMode.ALPHABETICAL)
                    onDismiss()
                }
            )

            SortOptionRow(
                title = stringResource(R.string.sort_mode_manual),
                isSelected = currentSortMode == SortMode.MANUAL,
                onClick = {
                    onSortModeSelected(SortMode.MANUAL)
                    onDismiss()
                }
            )
        }
    }
}

@Composable
private fun SortOptionRow(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.padding16, vertical = Dimens.padding8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
}
