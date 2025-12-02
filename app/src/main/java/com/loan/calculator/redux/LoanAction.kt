package com.loan.calculator.redux

sealed class LoanAction {
    data class SetLoanAmount(val amount: Int) : LoanAction()
    data class SetLoanPeriod(val period: Int) : LoanAction()
    object SubmitApplication : LoanAction()
    object SubmitApplicationSuccess : LoanAction()
    data class SubmitApplicationError(val error: String) : LoanAction()
    data class SetTheme(val isDark: Boolean) : LoanAction()
    object ResetSubmission : LoanAction()
}