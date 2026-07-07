package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RegistrationScreen(
    onBack: () -> Unit,
    onNavigateToLists: () -> Unit,
) {
    Column {
        Text("RegistrationScreen")
        Button(onClick = onBack) {
            Text("onBack")
        }
        Button(onClick = onNavigateToLists) {
            Text("ToLists")
        }
    }
}
