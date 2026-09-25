package com.jagl.exchangeapp

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.jagl.exchangeapp.ui.activity.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ExchangeE2ETest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testExchangeFlow() {

        composeTestRule.onNodeWithTag("from_currency_dropdown").performTextInput("USD")

        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule.onNodeWithTag("currency_item_USD").isDisplayed()
        }

        composeTestRule.onNodeWithTag("currency_item_USD").performClick()

        composeTestRule.onNodeWithTag("to_currency_dropdown").performTextInput("MXN")

        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule.onNodeWithTag("currency_item_MXN").isDisplayed()
        }

        composeTestRule.onNodeWithTag("currency_item_MXN").performClick()

        composeTestRule.onNodeWithTag("amount_input").performTextInput("100")

        composeTestRule.onNodeWithTag("calculate_button").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000L) {
            composeTestRule.onNodeWithTag("exchange_result").isDisplayed()
        }

        composeTestRule.onNodeWithTag("exchange_result").assertExists()
    }

    @Test
    fun testErrorDialogFlow() {

        composeTestRule.onNodeWithTag("calculate_button").performClick()

        composeTestRule.onNodeWithTag("error_alert").assertExists()

        composeTestRule.onNodeWithTag("close_error_button").performClick()

        composeTestRule.onNodeWithTag("error_alert").assertDoesNotExist()
    }
}
