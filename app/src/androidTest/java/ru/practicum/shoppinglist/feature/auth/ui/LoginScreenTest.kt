package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.elkabelaya.shared_tests.testUtils.TestConstants
import com.elkabelaya.shared_tests.testUtils.waitUntilMatches
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.get
import ru.practicum.shoppinglist.testUtils.LoginErrorDispatcher
import ru.practicum.shoppinglist.testUtils.LoginSuccessDispatcher
import ru.practicum.shoppinglist.testUtils.MockServer
import kotlin.test.assertTrue
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LoginScreenTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val mockWebServer = MockServer()
    private lateinit var loginScreenTestObject: LoginScreenTestObject

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        mockWebServer.setUp(composeTestRule)
        loginScreenTestObject = LoginScreenTestObject(composeTestRule)
    }

    @After
    fun tearDown() {
        mockWebServer.tearDown(composeTestRule)
    }

    @Test
    fun successLoginNavigatesToLists() = runTest {
        mockWebServer.setDispatcher(LoginSuccessDispatcher())
        loginScreenTestObject.doLogin()

        composeTestRule.awaitIdle()
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            loginScreenTestObject.navigatedToList
        }

        assertTrue(
            loginScreenTestObject.navigatedToList,
            "Navigation to Content must be invoked on success login"
        )
    }

    @Test
    fun failedLoginShowsError() = runTest {
        mockWebServer.setDispatcher(LoginErrorDispatcher(TestConstants.SERVER_ERROR_TEXT))
        loginScreenTestObject.doLogin()

        composeTestRule.awaitIdle()

        loginScreenTestObject.serverError.assert(
            hasText(TestConstants.SERVER_ERROR_TEXT, substring = true),
            { "Server error must be shown on failed login" }
        )
    }

    @Test
    fun wrongEmailShowsError() = runTest {
        loginScreenTestObject.enterEmail(TestConstants.INVALID_EMAIL)

        composeTestRule.awaitIdle()
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            try {
                loginScreenTestObject.emailInput.fetchSemanticsNode()
                    .config.contains(SemanticsProperties.Error)
            } catch (e: AssertionError) {
                false
            }
        }
        loginScreenTestObject.emailInput.assert(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.Error),
            { "Email field must show error on wrong value" }
        )
    }

    @Test
    fun smallPasswordShowsError() = runTest {
        loginScreenTestObject.enterPassword(TestConstants.SMALL_PASSWORD)

        composeTestRule.awaitIdle()
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            try {
                loginScreenTestObject.passwordInput.fetchSemanticsNode()
                    .config.contains(SemanticsProperties.Error)
            } catch (e: AssertionError) {
                false
            }
        }
        loginScreenTestObject.passwordInput.assert(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.Error),
            { "Password field must show error on wrong value" }
        )
    }

    @Test
    fun loginButtonEnabledWhenValidData() = runTest {
        loginScreenTestObject.enterEmail(TestConstants.VALID_EMAIL)
        loginScreenTestObject.enterPassword(TestConstants.VALID_PASSWORD)

        composeTestRule.awaitIdle()
        composeTestRule.waitUntilMatches(
            loginScreenTestObject.loginButton,
            isEnabled(),
            2000
        )

        loginScreenTestObject.loginButton.assert(isEnabled()) {
            "Login button must be enabled when all data correct"
        }
    }

    @Test
    fun loginButtonDisabledWhenNoData() = runTest {
        loginScreenTestObject.enterEmail("")
        loginScreenTestObject.enterPassword("")

        composeTestRule.awaitIdle()
        composeTestRule.waitUntilMatches(
            loginScreenTestObject.loginButton,
            isNotEnabled(),
            2000
        )
        loginScreenTestObject.loginButton.assert(isNotEnabled()) {
            "Login button must be disabled when no data"
        }
    }

    @Test
    fun loginButtonDisabledWhenWrongData() = runTest {
        loginScreenTestObject.enterEmail(TestConstants.INVALID_EMAIL)
        loginScreenTestObject.enterPassword(TestConstants.INVALID_PASSWORD)

        composeTestRule.awaitIdle()
        composeTestRule.waitUntilMatches(
            loginScreenTestObject.loginButton,
            isNotEnabled(),
            2000
        )
        loginScreenTestObject.loginButton.assert(isNotEnabled()) {
            "Login button must be disabled when wrong data"
        }
    }

    @Test
    fun registerButtonNavigatesToRegister() = runTest {
        loginScreenTestObject.registerButton.performScrollTo().performClick()

        composeTestRule.awaitIdle()
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            loginScreenTestObject.navigatedToRegister
        }
        assertTrue(
            loginScreenTestObject.navigatedToRegister,
            "Navigation to Register must be invoked on register click"
        )
    }

    @Test
    fun recoveryButtonNavigatesToRecovery() = runTest {
        loginScreenTestObject.recoveryButton.performScrollTo().performClick()

        composeTestRule.awaitIdle()
        composeTestRule.waitUntil(timeoutMillis = 2000) {
            loginScreenTestObject.navigatedToRecovery
        }
        assertTrue(
            loginScreenTestObject.navigatedToRecovery,
            "Navigation to Recovery must be invoked on recovery click"
        )
    }
}
