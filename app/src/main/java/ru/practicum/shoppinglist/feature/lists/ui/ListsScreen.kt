package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.practicum.shoppinglist.R
import ru.practicum.shoppinglist.core.ui.components.DropdownTextField
import ru.practicum.shoppinglist.core.ui.theme.Dimens

@Composable
fun ListsScreen(
    onNavigateToDetail: (id: Long) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Column {
        Button(onClick = { onNavigateToDetail(1) }) {
            Text("ListsScreen stub, click to detail")
        }
        DropdownTextField(
            value = text,
            {
                text = it
            },
            R.string.lists_add_label,
            R.string.lists_add_placeholder,
            listOf("item1", "item2"),
            Modifier.padding(Dimens.padding16),
            exposed = false,
            readOnly = true
        )
    }
}
