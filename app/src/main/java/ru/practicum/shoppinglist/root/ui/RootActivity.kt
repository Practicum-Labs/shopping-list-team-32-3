package ru.practicum.shoppinglist.root.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Единственная Activity-хост приложения. Весь UI строится на Compose.
 * Навигация подключается в [ComposeRoot] (см. T-02).
 */
class RootActivity : ComponentActivity() {
    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            keepSplash
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRoot(
                koinViewModel()
            ) {
                keepSplash = false
            }
        }
    }
}
