package ru.practicum.shoppinglist.feature.onboarding.ui

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnboardingScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun clickInvokesNavigation() = runBlocking {
        var navigated = false
        composeRule.setContent {
            OnboardingScreen(onNavigateToLists = { navigated = true })
        }

        composeRule.onNode(hasClickAction()).performClick()
        assert(navigated) { "Navigation callback should be invoked on click" }
    }

    @Test
    fun autoNavigateAfterDelay() = runBlocking {
        var navigated = false
        composeRule.setContent {
            OnboardingScreen(onNavigateToLists = { navigated = true })
        }

        composeRule.mainClock.advanceTimeBy(5001L)

        assert(navigated) { "Navigation callback should be invoked after delay" }
    }
}