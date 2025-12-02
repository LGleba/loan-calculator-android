package com.loan.calculator

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.loan.calculator.ui.theme.LoanCalculatorTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI тесты для Loan Calculator
 * Запускаются на реальном устройстве/эмуляторе
 *
 * Команда: ./gradlew connectedAndroidTest
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ==================== Basic Instrumented Test ====================

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.loan.calculator", appContext.packageName)
    }

    // ==================== Amount Slider Tests ====================

    @Test
    fun loanSlider_displaysInitialAmount() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        // Check if initial amount is displayed
        composeTestRule.onNodeWithText("5 000", substring = true).assertExists()
    }

    @Test
    fun loanSlider_displaysMinMaxLabels() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("5 000", substring = true).assertExists()
        composeTestRule.onNodeWithText("50 000", substring = true).assertExists()
    }

    // ==================== Period Selector Tests ====================

    @Test
    fun periodSelector_displaysAllOptions() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("7 d").assertExists()
        composeTestRule.onNodeWithText("14 d").assertExists()
        composeTestRule.onNodeWithText("21 d").assertExists()
        composeTestRule.onNodeWithText("28 d").assertExists()
    }

    @Test
    fun periodSelector_canSelect7Days() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("7 d").performClick()
        composeTestRule.onNodeWithText("7 d").assertExists()
    }

    @Test
    fun periodSelector_canSelect21Days() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("21 d").performClick()
        composeTestRule.onNodeWithText("21 d").assertExists()
    }

    // ==================== Calculation Cards Tests ====================

    @Test
    fun calculationCard_displaysInterestRate() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("Interest Rate", substring = true).assertExists()
    }

    @Test
    fun calculationCard_displaysTotalRepayment() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("Total Repayment", substring = true).assertExists()
    }

    // ==================== Submit Button Tests ====================

    @Test
    fun submitButton_exists() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("Submit").assertExists()
    }

    @Test
    fun submitButton_isClickable() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("Submit")
            .assertExists()
            .assertIsEnabled()
    }

    // ==================== Theme Toggle Tests ====================

    @Test
    fun themeToggle_buttonExists() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        // Check if theme toggle exists
        composeTestRule.onNodeWithContentDescription("Toggle theme", substring = true)
            .assertExists()
    }

    // ==================== Integration Tests ====================

    @Test
    fun fullFlow_selectPeriod_andVerifyRate() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        // Select 21 days period
        composeTestRule.onNodeWithText("21 d").performClick()
        composeTestRule.waitForIdle()

        // Verify interest rate changed to 15.75%
        composeTestRule.onNodeWithText("15.75%", substring = true).assertExists()
    }

    @Test
    fun fullFlow_allPeriods_calculateCorrectRates() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        // Test 7 days -> 14.25%
        composeTestRule.onNodeWithText("7 d").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("14.25%", substring = true).assertExists()

        // Test 14 days -> 15.00%
        composeTestRule.onNodeWithText("14 d").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("15.00%", substring = true).assertExists()

        // Test 21 days -> 15.75%
        composeTestRule.onNodeWithText("21 d").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("15.75%", substring = true).assertExists()

        // Test 28 days -> 16.50%
        composeTestRule.onNodeWithText("28 d").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("16.50%", substring = true).assertExists()
    }

    // ==================== Accessibility Tests ====================

    @Test
    fun accessibility_allButtonsAreClickable() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        composeTestRule.onNodeWithText("7 d").assertHasClickAction()
        composeTestRule.onNodeWithText("14 d").assertHasClickAction()
        composeTestRule.onNodeWithText("21 d").assertHasClickAction()
        composeTestRule.onNodeWithText("28 d").assertHasClickAction()
        composeTestRule.onNodeWithText("Submit").assertHasClickAction()
    }

    // ==================== Edge Cases ====================

    @Test
    fun edgeCase_initialStateIsValid() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        // Initial amount should be visible
        composeTestRule.onNodeWithText("5 000", substring = true).assertExists()

        // Initial period 14 should be selected
        composeTestRule.onNodeWithText("14 d").assertExists()

        // Initial rate 15% should be displayed
        composeTestRule.onNodeWithText("15.00%", substring = true).assertExists()
    }

    @Test
    fun edgeCase_canSwitchBetweenAllPeriods() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        // Switch to each period sequentially
        listOf("7 d", "14 d", "21 d", "28 d").forEach { period ->
            composeTestRule.onNodeWithText(period).performClick()
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText(period).assertExists()
        }
    }

    // ==================== Performance Tests ====================

    @Test
    fun performance_periodSelectorResponseTime() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        val startTime = System.currentTimeMillis()

        composeTestRule.onNodeWithText("21 d").performClick()
        composeTestRule.waitForIdle()

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Should respond within 100ms
        assertTrue("Period selector response too slow: ${duration}ms", duration < 100)
    }

    @Test
    fun performance_themeToggleResponseTime() {
        composeTestRule.setContent {
            LoanCalculatorTheme {
                // Your LoanCalculatorScreen here
            }
        }

        val startTime = System.currentTimeMillis()

        composeTestRule.onNodeWithContentDescription("Toggle theme", substring = true)
            .performClick()
        composeTestRule.waitForIdle()

        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime

        // Should respond within 50ms
        assertTrue("Theme toggle response too slow: ${duration}ms", duration < 50)
    }
}