package com.loan.calculator.redux

import java.time.LocalDate

data class LoanState(
    val loanAmount: Int = 10000,
    val loanPeriod: Int = 14,
    val interestRate: Double = 15.0,
    val totalRepayment: Double = 11500.0,
    val repaymentDate: LocalDate = LocalDate.now().plusDays(14),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val isDarkTheme: Boolean = false
) {
    fun isValid(): Boolean {
        return loanAmount in 5000..50000 &&
                loanPeriod in listOf(7, 14, 21, 28)
    }
}