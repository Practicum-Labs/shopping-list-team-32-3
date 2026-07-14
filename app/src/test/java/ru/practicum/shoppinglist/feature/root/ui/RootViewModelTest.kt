package ru.practicum.shoppinglist.feature.root.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import ru.practicum.shoppinglist.root.domain.api.OnboardingRepository
import ru.practicum.shoppinglist.root.ui.InitialState
import ru.practicum.shoppinglist.root.ui.RootViewModel


import app.cash.turbine.test
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import ru.practicum.shoppinglist.core.domain.api.AuthRepository

@OptIn(ExperimentalCoroutinesApi::class)
class RootViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private var mockOnboardingRepo = mockk<OnboardingRepository>(relaxed = true)
    private val mockAuthRepo = mockk<AuthRepository>(relaxed = true)

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

        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo)
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

        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo)

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

        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo)

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
        RootViewModel(mockOnboardingRepo, mockAuthRepo)

        advanceUntilIdle()
        coVerify(exactly = 1) { mockOnboardingRepo.setOnboardPassed() }
    }
}
