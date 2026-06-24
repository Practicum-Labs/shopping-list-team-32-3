package ru.practicum.shoppinglist.feature.lists.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ListsScreen(
    onNavigateToDetail: (id: Long) -> Unit
) {
    Button(onClick = { onNavigateToDetail(1) }) {
        Text("ListsScreen stub, click to detail")
    }
}
