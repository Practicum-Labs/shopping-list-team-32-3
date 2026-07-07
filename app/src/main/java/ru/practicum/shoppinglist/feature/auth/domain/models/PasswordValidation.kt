package ru.practicum.shoppinglist.feature.auth.domain.models

data class PasswordValidation(
    val isLengthValid: Boolean,
    val hasUppercase: Boolean,
    val hasLowercase: Boolean,
    val hasDigit: Boolean,
    val hasSpecialChar: Boolean
) {
    val isValid: Boolean
        get() = isLengthValid && hasUppercase && hasLowercase && hasDigit && hasSpecialChar

    fun getStrength(): PasswordStrength {
        val booleans = listOf(
            isLengthValid,
            hasUppercase,
            hasLowercase,
            hasDigit,
            hasSpecialChar
        )

        var packedBits = 0
        for (i in booleans.indices) {
            if (booleans[i]) {
                packedBits = packedBits or (1 shl i)
            }
        }

        val trueCount = packedBits.countOneBits().toDouble()
        return when (trueCount / booleans.count()) {
            0.0 -> PasswordStrength.NONE
            1.0 -> PasswordStrength.STRONG
            in 0.0..0.333 -> PasswordStrength.WEAK
            else -> PasswordStrength.MEDIUM
        }
    }
}
