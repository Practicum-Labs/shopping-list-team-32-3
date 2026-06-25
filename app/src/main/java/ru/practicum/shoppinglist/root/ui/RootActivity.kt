package ru.practicum.shoppinglist.root.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Единственная Activity-хост приложения. Весь UI строится на Compose.
 * Навигация подключается в [ComposeRoot] (см. T-02).
 */
class RootActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRoot()
        }
    }
}
