package com.loan.calculator.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loan.calculator.network.RetrofitClient
import com.loan.calculator.redux.LoanAction
import com.loan.calculator.redux.LoanStore
import com.loan.calculator.repository.LoanRepository
import com.loan.calculator.utils.PreferencesManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoanCalculatorViewModel(context: Context) : ViewModel() {
    private val store = LoanStore()
    private val repository = LoanRepository(RetrofitClient.apiService)
    private val preferencesManager = PreferencesManager(context)

    val state: StateFlow<com.loan.calculator.redux.LoanState> = store.stateFlow

    init {
        // Load saved preferences
        val savedAmount = preferencesManager.getLoanAmount()
        val savedPeriod = preferencesManager.getLoanPeriod()
        val savedTheme = preferencesManager.getThemePreference()

        store.dispatch(LoanAction.SetLoanAmount(savedAmount))
        store.dispatch(LoanAction.SetLoanPeriod(savedPeriod))
        store.dispatch(LoanAction.SetTheme(savedTheme))
    }

    fun setLoanAmount(amount: Int) {
        store.dispatch(LoanAction.SetLoanAmount(amount))
        preferencesManager.saveLoanAmount(amount)
    }

    fun setLoanPeriod(period: Int) {
        store.dispatch(LoanAction.SetLoanPeriod(period))
        preferencesManager.saveLoanPeriod(period)
    }

    fun submitApplication() {
        if (!store.currentState.isValid()) {
            store.dispatch(
                LoanAction.SubmitApplicationError("Invalid loan parameters")
            )
            return
        }

        store.dispatch(LoanAction.SubmitApplication)

        viewModelScope.launch {
            try {
                val currentState = store.currentState
                val result = repository.submitApplication(
                    amount = currentState.loanAmount,
                    period = currentState.loanPeriod,
                    totalRepayment = currentState.totalRepayment
                )

                if (result.isSuccess) {
                    store.dispatch(LoanAction.SubmitApplicationSuccess)
                } else {
                    val errorMsg = result.exceptionOrNull()?.message
                        ?: "Unknown error occurred"
                    store.dispatch(LoanAction.SubmitApplicationError(errorMsg))
                }
            } catch (e: Exception) {
                store.dispatch(
                    LoanAction.SubmitApplicationError(e.message ?: "Network error")
                )
            }
        }
    }

    fun setTheme(isDark: Boolean) {
        store.dispatch(LoanAction.SetTheme(isDark))
        preferencesManager.saveThemePreference(isDark)
    }

    fun resetSubmission() {
        store.dispatch(LoanAction.ResetSubmission)
    }
}