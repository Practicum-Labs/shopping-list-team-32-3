package ru.practicum.shoppinglist.feature.auth.ui

import android.util.Patterns
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.elkabelaya.shared_tests.testUtils.TestConstants
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.practicum.shoppinglist.core.domain.api.AuthRepository
import ru.practicum.shoppinglist.core.domain.exception.DataException

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    val mockAuthRepository = mockk<AuthRepository>(relaxed = true)

    lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(SavedStateHandle(), mockAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkStatic(Patterns::class)
    }

    @Test
    fun validateEmailNoErrorWhenEmpty() = runTest {
        viewModel.onIntent(LoginContract.Intent.ValidateEmail)

        advanceUntilIdle()
        assertNull(
            "Email error must be hidden while emailEmpty",
            viewModel.state.value.emailErrorId
        )
    }

    @Test
    fun validateEmailNoErrorBeforeValidate() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.INVALID_EMAIL)

        advanceUntilIdle()
        assertNull(
            "Email error must be hidden before validate called",
            viewModel.state.value.emailErrorId
        )
    }

    @Test
    fun validateEmailNoErrorWhenValid() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_EMAIL)
        viewModel.onIntent(LoginContract.Intent.ValidateEmail)

        advanceUntilIdle()
        assertNull(
            "Email error must be hidden if email is valid",
            viewModel.state.value.emailErrorId
        )
    }

    @Test
    fun changeEmailClearsErrorWhenEmailBecomesValid() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.INVALID_EMAIL)
        viewModel.onIntent(LoginContract.Intent.ValidateEmail)
        viewModel.state.value.email.edit {
            delete(0, length)
            append(TestConstants.VALID_EMAIL)
        }
        advanceUntilIdle()

        assertNull(viewModel.state.value.emailErrorId)
    }

    @Test
    fun validatePasswordSetErrorWhenNotValid() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.INVALID_PASSWORD)
        viewModel.onIntent(LoginContract.Intent.ValidateEmail)
        advanceUntilIdle()
        assertNotNull(
            "Password error must be shown if password is invalid",
            viewModel.state.value.emailErrorId
        )
    }

    @Test
    fun validatePasswordNoErrorWhenValid() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_PASSWORD)
        viewModel.onIntent(LoginContract.Intent.ValidatePassword)
        advanceUntilIdle()
        assertNull(
            "Password error must be hidden if password is valid",
            viewModel.state.value.passwordErrorId
        )
    }

    @Test
    fun changePasswordClearsErrorWhenPasswordBecomesValid() = runTest {
        viewModel.state.value.password.setTextAndPlaceCursorAtEnd(TestConstants.INVALID_PASSWORD)
        viewModel.onIntent(LoginContract.Intent.ValidateEmail)
        viewModel.state.value.password.edit {
            delete(0, length)
            append(TestConstants.VALID_PASSWORD)
        }
        advanceUntilIdle()

        assertNull(viewModel.state.value.passwordErrorId)
    }

    @Test
    fun enterDisabledOnEmptyFields() = runTest {
        advanceUntilIdle()
        assertFalse(viewModel.state.value.enterEnabled)
    }

    @Test
    fun enterDisabledOnWrongEmail() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.INVALID_EMAIL)
        viewModel.state.value.password.setTextAndPlaceCursorAtEnd(TestConstants.VALID_PASSWORD)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.enterEnabled)
    }

    @Test
    fun enterDisabledOnWrongPassword() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_EMAIL)
        viewModel.state.value.password.setTextAndPlaceCursorAtEnd(TestConstants.INVALID_PASSWORD)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.enterEnabled)
    }

    @Test
    fun enterEnabledOnValidFields() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_EMAIL)
        viewModel.state.value.password.setTextAndPlaceCursorAtEnd(TestConstants.VALID_PASSWORD)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.enterEnabled)
    }

    @Test
    fun registerIntentSendsNavigateToRegistrationEffect() = runTest {
        viewModel.effects.test {
            viewModel.onIntent(LoginContract.Intent.Register)
            val effect = awaitItem()
            assertEquals(
                LoginContract.Effect.NavigateToRegistration,
                effect
            )
        }
    }

    @Test
    fun recoveryIntentSendsNavigateToRecoveryEffect() = runTest {
        viewModel.effects.test {
            viewModel.onIntent(LoginContract.Intent.Recovery)
            val effect = awaitItem()
            assertEquals(
                LoginContract.Effect.NavigateToRecovery,
                effect
            )
        }
    }

    @Test
    fun enterSetsLoadingTrue() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_EMAIL)
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_PASSWORD)
        viewModel.onIntent(LoginContract.Intent.Enter)

        advanceUntilIdle()
        assertEquals(false, viewModel.state.value.isLoading)
    }

    @Test
    fun enterSuccessfulLoginTriggersNavigateToListsEffect() = runTest {
        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_EMAIL)
        viewModel.state.value.password.setTextAndPlaceCursorAtEnd(TestConstants.VALID_PASSWORD)

        coEvery { mockAuthRepository.login(any(), any()) } returns Unit
        advanceUntilIdle()
        viewModel.effects.test {
            viewModel.onIntent(LoginContract.Intent.Enter)
            val effect = awaitItem()
            assertEquals(LoginContract.Effect.NavigateToLists, effect)
        }
    }

    @Test
    fun enterNetworkErrorShowsTotalErrorAndStopsLoading() = runTest {
        val errorMsg = TestConstants.SERVER_ERROR_TEXT
        coEvery {
            mockAuthRepository.login(any(), any())
        } throws DataException.Network(errorMsg)

        viewModel.state.value.email.setTextAndPlaceCursorAtEnd(TestConstants.VALID_EMAIL)
        viewModel.state.value.password.setTextAndPlaceCursorAtEnd(TestConstants.VALID_PASSWORD)
        advanceUntilIdle()

        viewModel.onIntent(LoginContract.Intent.Enter)
        advanceUntilIdle()

        assertEquals(false, viewModel.state.value.isLoading)
        assertEquals(errorMsg, viewModel.state.value.totalError)
    }
}
