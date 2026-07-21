package com.elkabelaya.shared_tests.testUtils

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.ComposeTestRule


@Suppress("SwallowedException")
fun ComposeTestRule.waitUntilMatches(
    interaction: SemanticsNodeInteraction,
    matcher: androidx.compose.ui.test.SemanticsMatcher,
    timeoutMillis: Long = 2000
) {
    this.waitUntil(timeoutMillis) {
        try {
            interaction.assert(matcher)
            true
        } catch (e: AssertionError) {
            false
        }
    }
}
