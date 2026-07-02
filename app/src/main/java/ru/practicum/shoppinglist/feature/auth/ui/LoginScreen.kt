package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    onNavigateToRegistration: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onNavigateToLists: () -> Unit
){
    Column {
        Text("LoginScreen")
        Button(onClick = onNavigateToRegistration){
            Text("ToRegistration")
        }
        Button(onClick = onNavigateToRecovery){
            Text("ToRecovery")
        }
        Button(onClick = onNavigateToLists){
            Text("ToLists")
        }
    }
}