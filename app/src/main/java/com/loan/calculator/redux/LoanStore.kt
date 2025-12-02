package com.loan.calculator.redux

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoanStore {
    private val _stateFlow = MutableStateFlow(LoanState())
    val stateFlow: StateFlow<LoanState> = _stateFlow.asStateFlow()

    val currentState: LoanState
        get() = _stateFlow.value

    fun dispatch(action: LoanAction) {
        val newState = LoanReducer.reduce(_stateFlow.value, action)
        _stateFlow.value = newState
    }
}