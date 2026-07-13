package ru.practicum.shoppinglist.testUtils

import androidx.compose.ui.test.performTextInput

object TestConstants {
    const val VALID_EMAIL = "user@example.com"
    const val VALID_PASSWORD = "Password123!"

    const val INVALID_EMAIL = "user@example"
    const val SMALL_PASSWORD = "pas"
    const val INVALID_PASSWORD = "password"

    const val SERVER_ERROR_TEXT = "Some server error text"
}