package com.loan.calculator.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

data class LoanApplicationRequest(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("period")
    val period: Int,
    @SerializedName("totalRepayment")
    val totalRepayment: Double
)

data class LoanApplicationResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("body")
    val body: String? = null
)

interface ApiService {
    @POST("posts")
    suspend fun submitLoanApplication(
        @Body request: LoanApplicationRequest
    ): LoanApplicationResponse
}