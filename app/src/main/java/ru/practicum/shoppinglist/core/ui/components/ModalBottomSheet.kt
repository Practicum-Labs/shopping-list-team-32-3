package ru.practicum.shoppinglist.core.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults.DragHandle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import ru.practicum.shoppinglist.core.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        scrimColor = MaterialTheme.colorScheme.scrim,
        dragHandle = {
            DragHandle(
                color = MaterialTheme.colorScheme.outline,
            )
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@AppPreview
@Composable
private fun ModalBottomSheetPreview() {
    AppTheme {
        AppModalBottomSheet({}) {
            Text("Some Content ")
        }
    }
}
