package com.loan.calculator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loan.calculator.ui.components.CalculationCard
import com.loan.calculator.ui.components.LoanSlider
import com.loan.calculator.ui.components.PeriodSlider
import com.loan.calculator.ui.components.ResultDialog
import com.loan.calculator.utils.DateUtils
import com.loan.calculator.utils.NumberFormatter
import com.loan.calculator.viewmodel.LoanCalculatorViewModel

@Composable
fun LoanCalculatorScreen(viewModel: LoanCalculatorViewModel) {
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    LoanCalculatorContent(
        state = currentState,
        onAmountChanged = { viewModel.setLoanAmount(it) },
        onPeriodChanged = { viewModel.setLoanPeriod(it) },
        onSubmit = { viewModel.submitApplication() },
        onThemeToggle = { viewModel.setTheme(it) },
        onDismissResult = { viewModel.resetSubmission() }
    )
}

@Composable
private fun LoanCalculatorContent(
    state: com.loan.calculator.redux.LoanState,
    onAmountChanged: (Int) -> Unit,
    onPeriodChanged: (Int) -> Unit,
    onSubmit: () -> Unit,
    onThemeToggle: (Boolean) -> Unit,
    onDismissResult: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loan Calculator",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                IconButton(onClick = { onThemeToggle(!state.isDarkTheme) }) {
                    Text(
                        text = if (state.isDarkTheme) "☀️" else "🌙",
                        fontSize = 24.sp
                    )
                }
            }

            LoanSlider(
                value = state.loanAmount.toFloat(),
                onValueChange = { onAmountChanged(it.toInt()) },
                valueRange = 5000f..50000f,
                steps = 44,
                header = "How much ?",
                label = NumberFormatter.formatAmount(state.loanAmount),
                sliderColor = Color(0xFF008000),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            PeriodSlider(
                selectedPeriod = state.loanPeriod,
                periods = listOf(7, 14, 21, 28),
                header = "How long ?",
                onPeriodChanged = onPeriodChanged,
                sliderColor = Color(0xFFFFA500),
                minLabel = "7",
                maxLabel = "28",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            // Calculation Cards
            CalculationCard(
                title = "Interest Rate",
                value = NumberFormatter.formatPercent(state.interestRate),
                icon = "📊",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            CalculationCard(
                title = "Total Repayment",
                value = NumberFormatter.formatCurrency(state.totalRepayment),
                icon = "💰",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            CalculationCard(
                title = "Repayment Date",
                value = DateUtils.formatDate(state.repaymentDate),
                icon = "📅",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            // Submit Button
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Submit Application",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Result Dialog
        if (state.isSuccess || state.errorMessage != null) {
            ResultDialog(
                isSuccess = state.isSuccess,
                message = state.errorMessage ?: "Application submitted successfully!",
                onDismiss = onDismissResult
            )
        }
    }
}