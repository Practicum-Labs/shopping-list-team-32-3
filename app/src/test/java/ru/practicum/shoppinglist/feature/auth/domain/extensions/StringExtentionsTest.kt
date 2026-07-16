package ru.practicum.shoppinglist.feature.auth.domain.extensions

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.practicum.shoppinglist.feature.auth.domain.extentions.isValidEmail
import ru.practicum.shoppinglist.feature.auth.domain.extentions.isValidPassword
import ru.practicum.shoppinglist.feature.auth.domain.models.PasswordStrength
import java.util.regex.Matcher
import java.util.regex.Pattern

class StringExtentionsTest {
    private var mockEmailPattern = mockk<Pattern>(relaxed = true)
    private var emailMatcher = mockk<Matcher>(relaxed = true)

    private fun setUpMatcher(value: Boolean) {
        coEvery {
            emailMatcher.matches()
        } returns value
        coEvery {
            mockEmailPattern.matcher(any())
        } returns emailMatcher
    }

    @Test
    fun isValidEmailReturnsTrueForCorrectAddress() = runTest {
        val email = "user@example.com"
        setUpMatcher(true)
        assertTrue(email.isValidEmail(mockEmailPattern))
    }

    @Test
    fun isValidEmailReturnsFalseForEmptyString() = runTest {
        val email = ""
        setUpMatcher(true)
        assertFalse(email.isValidEmail(mockEmailPattern))
    }

    @Test
    fun isValidEmailReturnsFalseIfNotMatches() = runTest {
        val email = "userexample.com"
        setUpMatcher(false)
        assertFalse(email.isValidEmail(mockEmailPattern))
    }

    @Test
    fun isValidPasswordTrueWhenLongEnough() = runTest {
        val password = "Aa1!abcd"
        assertTrue(password.isValidPassword().isLengthValid)
        assertTrue(password.isValidPassword().isValid)
    }

    @Test
    fun isValidPasswordFalseWhenTooShort() = runTest {
        val password = "Aa1!a"
        assertFalse(password.isValidPassword().isLengthValid)
        assertFalse(password.isValidPassword().isLengthValid)
    }

    @Test
    fun isValidPasswordHasUppercaseTrueWhenContainsUpper() = runTest {
        val password = "aa1!aA"
        assertTrue(password.isValidPassword().hasUppercase)
    }

    @Test
    fun isValidPasswordHasUppercaseFalseWhenNone() = runTest {
        val password = "aa1!aaa"
        assertFalse(password.isValidPassword().hasUppercase)
    }

    @Test
    fun isValidPasswordHasLowercaseTrueWhenContainsLower() = runTest {
        val password = "aa1!aaa"
        assertTrue(password.isValidPassword().hasLowercase)
    }

    @Test
    fun isValidPasswordHasLowercaseFalseWhenNone() = runTest {
        val password = "AA1!AAA"
        assertFalse(password.isValidPassword().hasLowercase)
    }

    @Test
    fun isValidPasswordHasDigitTrueWhenContainsNumber() = runTest {
        val password = "Aa!aBb"
        assertFalse(password.isValidPassword().hasDigit)
    }

    @Test
    fun isValidPasswordHasSpecialCharTrueWhenContainsNonAlnum() = runTest {
        val password = "Aa1!aaa"
        assertTrue(password.isValidPassword().hasSpecialChar)
    }

    @Test
    fun isValidPasswordStrong() = runTest {
        val v = "Aa1!abcd".isValidPassword()
        assertTrue(v.isLengthValid)
        assertTrue(v.hasUppercase)
        assertTrue(v.hasLowercase)
        assertTrue(v.hasDigit)
        assertTrue(v.hasSpecialChar)
        assertEquals(v.getStrength(), PasswordStrength.STRONG)
    }

    @Test
    fun isValidPasswordStrengthWeakIf2FlagsValid() = runTest {
        mapOf(
            "abcde" to "Must be weak if only lowercased symbols",
            "abcdef" to "Must be weak if only lowercased symbols and enough length",
            "ABCDEF" to "Must be weak if only uppercase symbols and enough length",
            "!!!!!!" to "Must be weak if only special  symbols and enough length",
            "123456" to "Must be weak if only numbers and enough length",
            "Qw" to "Must be weak if only lowercased and uppercase symbols",
            "Q!" to "Must be weak if only uppercase and special symbols",
            "w!" to "Must be weak if only lowercased and special symbols",
            "Q1" to "Must be weak if only uppercase and number symbols",
            "w1" to "Must be weak if only lowercased and number symbols",
            "!1" to "Must be weak if only special and number symbols",
        ).forEach { (testValue, message) ->
            assertEquals(
                message,
                PasswordStrength.WEAK,
                testValue.isValidPassword().getStrength()
            )
        }
    }

    @Test
    fun isValidPasswordStrengthMediumIf2to4FlagsValid() = runTest {
        mapOf(
            "qwertyQw1" to "Must be medium if length, upper, lower, number valid",
            "qwertyQw!" to "Must be medium if length, upper, lower, spec valid",

            "QWERTYQ!1" to "Must be medium if length, upper, spec, number valid",
            "q123456!" to "Must be medium if length, spec, lower, number valid",

            "!Qw1" to "Must be medium if spec, upper, lower, number valid",

            "qwertyQw" to "Must be medium if length, upper, lower valid",
            "QWERTYQW1" to "Must be medium if length, upper, number valid",
            "qwertyqw1" to "Must be medium if length, lower, number valid",
            "Qw1" to "Must be medium if lower, upper, number valid",

            "QWERTYQW!" to "Must be medium if length, upper, spec valid",
            "qwertyqw!" to "Must be medium if length, lower, spec valid",
            "Qw!" to "Must be medium if upper, lower, spec valid",

            "123456#" to "Must be medium if length, number, spec valid",
            "W1!" to "Must be medium if upper, number, spec valid",
        ).forEach { (testValue, message) ->

            assertEquals(
                message,
                PasswordStrength.MEDIUM,
                testValue.isValidPassword().getStrength()
            )
        }
    }
}
