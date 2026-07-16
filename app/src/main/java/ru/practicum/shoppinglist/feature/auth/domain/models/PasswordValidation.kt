package ru.practicum.shoppinglist.feature.auth.domain.models

data class PasswordValidation(
    val isLengthValid: Boolean,
    val hasUppercase: Boolean,
    val hasLowercase: Boolean,
    val hasDigit: Boolean,
    val hasSpecialChar: Boolean
) {
    val isValid: Boolean
        get() = isLengthValid

    fun getStrength(): PasswordStrength {
        val booleans = listOf(
            isLengthValid,
            hasUppercase,
            hasLowercase,
            hasDigit,
            hasSpecialChar
        )

        var trueCount = 0.0
        for (i in booleans.indices) {
            if (booleans[i]) {
                trueCount += 1
            }
        }
        return when (trueCount / booleans.count()) {
            0.0 -> PasswordStrength.NONE
            1.0 -> PasswordStrength.STRONG
            in 0.0..0.41 -> PasswordStrength.WEAK
            else -> PasswordStrength.MEDIUM
        }
    }
}
