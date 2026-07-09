package ru.practicum.shoppinglist.feature.root.ui

import ru.practicum.shoppinglist.feature.auth.domain.api.AuthRepository
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalCoroutinesApi::class)
class RootViewModelTest {

    private var mockOnboardingRepo = mockk<OnboardingRepository>(relaxed = true)
    private val mockAuthRepo = mockk<AuthRepository>(relaxed = true)

    @After
    fun tearDown() {
        clearMocks(mockOnboardingRepo, mockAuthRepo)
    }
        @Test
    fun initialStateOnboarding() = runTest {
        coEvery { mockOnboardingRepo.getOnboardPassed() } returns false
        coEvery { mockAuthRepo.check() } returns false

        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo)

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

        vm.state.test {
            val state = awaitItem()
            assert(state.initialState == InitialState.CONTENT)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setOnboardPassedOnce() = runTest {
        coEvery { mockOnboardingRepo.getOnboardPassed() } returns false
        val vm = RootViewModel(mockOnboardingRepo, mockAuthRepo)

        coVerify(exactly = 1) { mockOnboardingRepo.setOnboardPassed() }
    }
}
