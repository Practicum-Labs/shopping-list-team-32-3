package ru.practicum.shoppinglist.feature.listdetail.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ListDetailScreen(
    onBack: () -> Unit
) {
    Button(onClick = onBack) {
        Text("ListDetailScreen stub, click to back")
    }
}
