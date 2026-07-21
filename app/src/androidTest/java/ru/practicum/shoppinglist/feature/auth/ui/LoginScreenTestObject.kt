package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import com.elkabelaya.shared_tests.testUtils.TestConstants
import ru.practicum.shoppinglist.feature.auth.ui.tags.LoginTags

class LoginScreenTestObject(val composeTestRule: ComposeContentTestRule) {
    val emailInput = composeTestRule.onNodeWithTag(LoginTags.EMAIL_FIELD)
    val passwordInput = composeTestRule.onNodeWithTag(LoginTags.PASSWORD_FIELD)
    val loginButton = composeTestRule.onNodeWithTag(LoginTags.LOGIN_BUTTON)
    val serverError = composeTestRule.onNodeWithTag(LoginTags.SERVER_ERROR)
    val registerButton = composeTestRule.onNodeWithTag(LoginTags.REGISTER_BUTTON)
    val recoveryButton = composeTestRule.onNodeWithTag(LoginTags.RECOVERY_BUTTON)
    var navigatedToList: Boolean = false
    var navigatedToRegister: Boolean = false
    var navigatedToRecovery: Boolean = false

    init {
        composeTestRule.setContent {
            LoginScreen(
                onNavigateToLists = {
                    navigatedToList = true
                },
                onNavigateToRecovery = {
                    navigatedToRecovery = true
                },
                onNavigateToRegistration = {
                    navigatedToRegister = true
                }
            )
        }
    }

    fun doLogin() {
        emailInput.performTextInput(TestConstants.VALID_EMAIL)
        passwordInput.performTextInput(TestConstants.VALID_PASSWORD)
        loginButton.performClick()
    }

    fun enterEmail(text: String) {
        emailInput.performTextInput(text)
        emailInput.performImeAction()
    }

    fun enterPassword(text: String) {
        passwordInput.performTextInput(text)
        passwordInput.performImeAction()
    }
}
