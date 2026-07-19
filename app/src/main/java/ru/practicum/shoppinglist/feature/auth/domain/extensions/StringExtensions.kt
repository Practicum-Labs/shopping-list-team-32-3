package ru.practicum.shoppinglist.feature.auth.domain.extensions

import androidx.core.util.PatternsCompat
import ru.practicum.shoppinglist.feature.auth.domain.models.PasswordValidation

fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): PasswordValidation {
    return PasswordValidation(
        isLengthValid = this.length > 6,
        hasUppercase = this.any { it.isUpperCase() },
        hasLowercase = this.any { it.isLowerCase() },
        hasDigit = this.any { it.isDigit() },
        hasSpecialChar = this.any { !it.isLetterOrDigit() }
    )
}
