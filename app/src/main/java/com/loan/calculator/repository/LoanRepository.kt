package com.loan.calculator.repository

import com.loan.calculator.network.ApiService
import com.loan.calculator.network.LoanApplicationRequest
import com.loan.calculator.network.LoanApplicationResponse

class LoanRepository(private val apiService: ApiService) {
    suspend fun submitApplication(
        amount: Int,
        period: Int,
        totalRepayment: Double
    ): Result<LoanApplicationResponse> = try {
        val request = LoanApplicationRequest(
            amount = amount,
            period = period,
            totalRepayment = totalRepayment
        )
        val response = apiService.submitLoanApplication(request)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}