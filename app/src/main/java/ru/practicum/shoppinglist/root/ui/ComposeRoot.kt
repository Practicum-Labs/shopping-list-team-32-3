package ru.practicum.shoppinglist.root.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Корневой composable приложения — каркас.
 *
 * Тема приложения (T-03) и type-safe навигация (T-02) подключаются здесь позже.
 * Пока выводится заглушка, чтобы скелет собирался и запускался.
 */
@Composable
fun ComposeRoot() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Список покупок")
            }
        }
    }
}
