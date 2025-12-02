package com.loan.calculator.redux

import java.time.LocalDate

object LoanReducer {
    fun reduce(state: LoanState, action: LoanAction): LoanState {
        return when (action) {
            is LoanAction.SetLoanAmount -> {
                val amount = action.amount.coerceIn(5000, 50000)
                val (interestRate, totalRepayment) = calculateRepayment(
                    amount,
                    state.loanPeriod
                )
                state.copy(
                    loanAmount = amount,
                    interestRate = interestRate,
                    totalRepayment = totalRepayment,
                    repaymentDate = LocalDate.now().plusDays(state.loanPeriod.toLong()),
                    errorMessage = null
                )
            }
            is LoanAction.SetLoanPeriod -> {
                val period = action.period
                val (interestRate, totalRepayment) = calculateRepayment(
                    state.loanAmount,
                    period
                )
                state.copy(
                    loanPeriod = period,
                    interestRate = interestRate,
                    totalRepayment = totalRepayment,
                    repaymentDate = LocalDate.now().plusDays(period.toLong()),
                    errorMessage = null
                )
            }
            is LoanAction.SubmitApplication -> {
                state.copy(isLoading = true, errorMessage = null)
            }
            is LoanAction.SubmitApplicationSuccess -> {
                state.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = null
                )
            }
            is LoanAction.SubmitApplicationError -> {
                state.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = action.error
                )
            }
            is LoanAction.SetTheme -> {
                state.copy(isDarkTheme = action.isDark)
            }
            is LoanAction.ResetSubmission -> {
                state.copy(isSuccess = false, errorMessage = null)
            }
        }
    }

    private fun calculateRepayment(amount: Int, period: Int): Pair<Double, Double> {
        val baseRate = 15.0
        val periodMultiplier = when (period) {
            7 -> 0.95
            14 -> 1.0
            21 -> 1.05
            28 -> 1.1
            else -> 1.0
        }

        val interestRate = baseRate * periodMultiplier
        val interest = amount * (interestRate / 100.0) * (period / 365.0)
        val totalRepayment = amount + interest

        return Pair(interestRate, totalRepayment)
    }
}