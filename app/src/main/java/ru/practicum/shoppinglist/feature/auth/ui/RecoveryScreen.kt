package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RecoveryScreen(onBack: () -> Unit) {
    Column {
        Text("RecoveryScreen")
        Button(onClick = onBack){
            Text("onBack")
        }

    }
}
