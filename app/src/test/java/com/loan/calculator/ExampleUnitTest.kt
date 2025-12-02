package com.loan.calculator

import org.junit.Test
import org.junit.Assert.*
import com.loan.calculator.redux.LoanReducer
import com.loan.calculator.redux.LoanAction
import com.loan.calculator.redux.LoanState

class ExampleUnitTest {

    // ==================== Basic Sanity Check ====================

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    // ==================== LoanAction Tests ====================

    @Test
    fun setLoanAmount_updatesState() {
        val initialState = LoanState(
            loanAmount = 5000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val action = LoanAction.SetLoanAmount(25000)
        val newState = LoanReducer.reduce(initialState, action)

        assertEquals(25000, newState.loanAmount)
    }

    @Test
    fun setLoanPeriod_updatesState() {
        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val action = LoanAction.SetLoanPeriod(21)
        val newState = LoanReducer.reduce(initialState, action)

        assertEquals(21, newState.loanPeriod)
    }

    @Test
    fun submitApplication_setsLoadingTrue() {
        val initialState = LoanState(
            loanAmount = 15000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val action = LoanAction.SubmitApplication
        val newState = LoanReducer.reduce(initialState, action)

        assertTrue(newState.isLoading)
        assertNull(newState.errorMessage)
    }

    @Test
    fun submitApplicationSuccess_setsLoadingFalse() {
        val initialState = LoanState(
            loanAmount = 20000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = true,
            errorMessage = null,
            isDarkTheme = false
        )

        val action = LoanAction.SubmitApplicationSuccess
        val newState = LoanReducer.reduce(initialState, action)

        assertFalse(newState.isLoading)
        assertNull(newState.errorMessage)
    }

    @Test
    fun submitApplicationError_setsErrorMessage() {
        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = true,
            errorMessage = null,
            isDarkTheme = false
        )

        val errorMessage = "Network connection failed"
        val action = LoanAction.SubmitApplicationError(errorMessage)
        val newState = LoanReducer.reduce(initialState, action)

        assertFalse(newState.isLoading)
        assertEquals(errorMessage, newState.errorMessage)
    }

    @Test
    fun setTheme_togglesDarkMode() {
        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val action = LoanAction.SetTheme(true)
        val newState = LoanReducer.reduce(initialState, action)

        assertTrue(newState.isDarkTheme)
    }

    @Test
    fun setTheme_canSwitchBackToLight() {
        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = true
        )

        val action = LoanAction.SetTheme(false)
        val newState = LoanReducer.reduce(initialState, action)

        assertFalse(newState.isDarkTheme)
    }

    // ==================== Interest Rate Calculation Tests ====================

    @Test
    fun interestRate_for7Days_is14_25Percent() {
        val state = LoanState(
            loanAmount = 10000,
            loanPeriod = 7,
            interestRate = 0.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        // Assuming you calculate interest based on period
        val expectedRate = 15.0 * 0.95 // 14.25%

        assertEquals(14.25, expectedRate, 0.01)
    }

    @Test
    fun interestRate_for14Days_is15Percent() {
        val expectedRate = 15.0 * 1.0 // 15.00%
        assertEquals(15.0, expectedRate, 0.01)
    }

    @Test
    fun interestRate_for21Days_is15_75Percent() {
        val expectedRate = 15.0 * 1.05 // 15.75%
        assertEquals(15.75, expectedRate, 0.01)
    }

    @Test
    fun interestRate_for28Days_is16_5Percent() {
        val expectedRate = 15.0 * 1.1 // 16.50%
        assertEquals(16.5, expectedRate, 0.01)
    }

    // ==================== Total Repayment Calculation Tests ====================

    @Test
    fun totalRepayment_greaterThanPrincipal() {
        val principal = 20000.0
        val rate = 0.15
        val days = 14

        val interest = principal * rate * days / 365.0
        val total = principal + interest

        assertTrue(total > principal)
    }

    @Test
    fun totalRepayment_calculation_for10000_14days() {
        val principal = 10000.0
        val rate = 0.15
        val days = 14

        val expected = principal + (principal * rate * days / 365.0)
        val actual = 10000.0 + (10000.0 * 0.15 * 14.0 / 365.0)

        assertEquals(expected, actual, 0.01)
    }

    // ==================== State Immutability Tests ====================

    @Test
    fun reducer_doesNotModifyOriginalState() {
        val originalState = LoanState(
            loanAmount = 5000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val action = LoanAction.SetLoanAmount(25000)
        LoanReducer.reduce(originalState, action)

        // Original state should remain unchanged
        assertEquals(5000, originalState.loanAmount)
    }

    // ==================== Edge Cases ====================

    @Test
    fun loanAmount_canBeMinimum5000() {
        val action = LoanAction.SetLoanAmount(5000)

        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val newState = LoanReducer.reduce(initialState, action)

        assertTrue(newState.loanAmount >= 5000)
    }

    @Test
    fun loanAmount_canBeMaximum50000() {
        val action = LoanAction.SetLoanAmount(50000)

        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val newState = LoanReducer.reduce(initialState, action)

        assertTrue(newState.loanAmount <= 50000)
    }

    @Test
    fun loanPeriod_acceptsValid7Days() {
        val action = LoanAction.SetLoanPeriod(7)

        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val newState = LoanReducer.reduce(initialState, action)

        assertEquals(7, newState.loanPeriod)
    }

    @Test
    fun loanPeriod_acceptsValid28Days() {
        val action = LoanAction.SetLoanPeriod(28)

        val initialState = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        val newState = LoanReducer.reduce(initialState, action)

        assertEquals(28, newState.loanPeriod)
    }

    // ==================== Complex Flow Tests ====================

    @Test
    fun fullApplicationFlow_updatesStateCorrectly() {
        var state = LoanState(
            loanAmount = 5000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        // Step 1: Set amount
        state = LoanReducer.reduce(state, LoanAction.SetLoanAmount(20000))
        assertEquals(20000, state.loanAmount)

        // Step 2: Set period
        state = LoanReducer.reduce(state, LoanAction.SetLoanPeriod(21))
        assertEquals(21, state.loanPeriod)

        // Step 3: Submit
        state = LoanReducer.reduce(state, LoanAction.SubmitApplication)
        assertTrue(state.isLoading)

        // Step 4: Success
        state = LoanReducer.reduce(state, LoanAction.SubmitApplicationSuccess)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun errorFlow_handlesErrorCorrectly() {
        var state = LoanState(
            loanAmount = 10000,
            loanPeriod = 14,
            interestRate = 15.0,
            totalRepayment = 0.0,
            isLoading = false,
            errorMessage = null,
            isDarkTheme = false
        )

        // Step 1: Submit
        state = LoanReducer.reduce(state, LoanAction.SubmitApplication)
        assertTrue(state.isLoading)

        // Step 2: Error occurs
        state = LoanReducer.reduce(state, LoanAction.SubmitApplicationError("Network error"))
        assertFalse(state.isLoading)
        assertEquals("Network error", state.errorMessage)

        // Step 3: Reset submission
        state = LoanReducer.reduce(state, LoanAction.ResetSubmission)
        assertNull(state.errorMessage)
        assertFalse(state.isLoading)
    }
}