package ru.practicum.shoppinglist.feature.auth.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.IdlingResource
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import ru.practicum.shoppinglist.core.di.CoreDiKeys
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Before
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.get
import ru.practicum.shoppinglist.feature.auth.ui.tags.LoginTags
import ru.practicum.shoppinglist.testUtils.LoginSuccessDispatcher
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import ru.practicum.shoppinglist.testUtils.LoginErrorDispatcher
import ru.practicum.shoppinglist.testUtils.MockServer
import ru.practicum.shoppinglist.testUtils.TestConstants
import ru.practicum.shoppinglist.testUtils.waitUntilMatches
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LoginScreenTest: KoinTest {

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