package ru.practicum.shoppinglist.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.preview.DropdownTextFieldPreviewModel
import ru.practicum.shoppinglist.core.ui.components.preview.DropdownTextFieldPreviewProvider
import ru.practicum.shoppinglist.core.ui.theme.AppTheme
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    @StringRes placeholderId: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    exposed: Boolean = false,
    readOnly: Boolean = false,
    expanded: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    var isFocused by remember { mutableStateOf(false) }
    val selectedText = rememberTextFieldState(value)
    var changeBySelect by remember { mutableStateOf(false) }
    val filteredItems = if (readOnly) {
        items
    } else {
        items.filter {
            it.contains(selectedText.text, ignoreCase = true)
        }
    }
    LaunchedEffect(selectedText) {
        snapshotFlow { selectedText.text }
            .distinctUntilChanged()
            .collect {
                if (isFocused && !it.isEmpty() && !changeBySelect) {
                    expanded.value = true
                }
                changeBySelect = false
                onValueChange(it.toString())
            }
    }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value },
        modifier = modifier
    ) {
        AppTextField(
            selectedText,
            labelId,
            placeholderId,
            readOnly = readOnly,
            modifier = Modifier
                .menuAnchor(PrimaryEditable, true)
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (readOnly && isFocused) {
                        expanded.value = true
                    }
                },
            trailingIcon = {
                if (readOnly) {
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        Icon(
                            painter = painterResource(
                                id = if (expanded.value) R.drawable.ic_core_arrow_drop_up else R.drawable.ic_core_arrow_drop_down
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                        )
                    }
                }
            }
        )
        if (filteredItems.isNotEmpty()) {
            DropdownMenu(
                expanded = expanded.value,
                properties = PopupProperties(focusable = if (readOnly) true else false),
                onDismissRequest = { expanded.value = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier
                    .requiredSizeIn(maxHeight = 176.dp)
                    .then(
                        if (exposed) {
                            Modifier.exposedDropdownSize()
                        } else {
                            Modifier.width(IntrinsicSize.Max)
                        }
                    )

            ) {
                filteredItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.background(Color.Green)
                            )
                        },
                        onClick = {
                            changeBySelect = true
                            selectedText.setTextAndPlaceCursorAtEnd(item)
                            expanded.value = false
                        },
                        modifier = Modifier.height(40.dp),
                        contentPadding = PaddingValues(horizontal = Dimens.padding12)
                    )
                }
            }
        }
    }
}

@AppPreview
@Composable
private fun DropdownTextFieldPreview(
    @PreviewParameter(DropdownTextFieldPreviewProvider::class) model: DropdownTextFieldPreviewModel
) {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DropdownTextField(
                value = model.value,
                {},
                R.string.lists_add_label,
                R.string.lists_add_placeholder,
                listOf("item1", "item2"),
                Modifier.padding(Dimens.padding16)
                    .fillMaxWidth(),
                readOnly = model.readOnly,
                exposed = model.exposed
            )
        }
    }
}
