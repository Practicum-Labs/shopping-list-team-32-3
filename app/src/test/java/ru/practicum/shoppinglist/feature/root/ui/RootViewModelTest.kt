package ru.practicum.shoppinglist.feature.root.ui

import app.cash.turbine.test
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.practicum.shoppinglist.core.domain.api.AuthRepository
import ru.practicum.shoppinglist.root.domain.api.OnboardingRepository
import ru.practicum.shoppinglist.root.domain.api.ShakeRepository
import ru.practicum.shoppinglist.root.ui.InitialState
import ru.practicum.shoppinglist.root.ui.RootViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class RootViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private var mockOnboardingRepo = mockk<OnboardingRepository>(relaxed = true)
    private val mockAuthRepo = mockk<AuthRepository>(relaxed = true)
    private val mockShakeRepo = mockk<ShakeRepository>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(mockOnboardingRepo, mockAuthRepo)
    }

    @Test
    fun initialStateOnboarding() = runTest {
        coEvery { mockOnboardingRepo.getOnboardPassed() } returns false
        coEvery { mockAuthRepo.check() } returns false

        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo, mockShakeRepo)
        advanceUntilIdle()
        vm.state.test {
            val state = awaitItem()
            assert(state.initialState == InitialState.ONBOARDING)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initialStateAuth() = runTest {
        coEvery { mockOnboardingRepo.getOnboardPassed() } returns true
        coEvery { mockAuthRepo.check() } returns false

        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo, mockShakeRepo)

        advanceUntilIdle()
        vm.state.test {
            val state = awaitItem()
            assert(state.initialState == InitialState.AUTH)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun initialStateContent() = runTest {
        coEvery { mockOnboardingRepo.getOnboardPassed() } returns true
        coEvery { mockAuthRepo.check() } returns true

        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo, mockShakeRepo)

        advanceUntilIdle()
        vm.state.test {
            val state = awaitItem()
            assert(state.initialState == InitialState.CONTENT)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setOnboardPassedOnce() = runTest {
        coEvery { mockOnboardingRepo.getOnboardPassed() } returns false
        RootViewModel(mockOnboardingRepo, mockAuthRepo, mockShakeRepo)

        advanceUntilIdle()
        coVerify(exactly = 1) { mockOnboardingRepo.setOnboardPassed() }
    }
}
