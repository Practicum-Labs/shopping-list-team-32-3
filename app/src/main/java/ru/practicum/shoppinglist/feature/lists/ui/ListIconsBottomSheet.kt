package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.practicum.shoppinglist.core.ui.components.AppModalBottomSheet
import ru.practicum.shoppinglist.core.ui.components.AppPreview
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun ListIconsBottomSheet(onSelect: (key: AppIconKey) -> Unit, onDismiss: () -> Unit) {
    AppModalBottomSheet(
        onDismiss = onDismiss
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = Dimens.icon48),
            contentPadding = PaddingValues(Dimens.padding16),
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding16),
            verticalArrangement = Arrangement.spacedBy(Dimens.padding16),
            modifier = Modifier
        ) {
            items(items = AppIconKey.entries, key = { it }) { key ->
                FilledIconButton(
                    onClick = { onSelect(key) },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier
                        .padding(Dimens.padding4)

                ) {
                    Icon(
                        imageVector = iconForKey(key),
                        contentDescription = key.value,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(Dimens.icon24),
                    )
                }
            }
        }
    }
}

@AppPreview
@Composable
private fun ListIconsBottomSheetPreview() {
    AppTheme {
        ListIconsBottomSheet({}, {})
    }
}
