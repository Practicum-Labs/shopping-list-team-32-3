package ru.practicum.shoppinglist.feature.onboarding.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun OnboardingScreen(
    onNavigateToLists: () -> Unit
) {
    Button(onClick = onNavigateToLists) {
        Text("OnboardingScreen stub, click to next")
    }
}
