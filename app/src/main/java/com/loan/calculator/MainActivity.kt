package com.loan.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.loan.calculator.ui.screens.LoanCalculatorScreen
import com.loan.calculator.ui.theme.LoanCalculatorTheme
import com.loan.calculator.viewmodel.LoanCalculatorViewModel
import com.loan.calculator.viewmodel.LoanCalculatorViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this,
            LoanCalculatorViewModelFactory(this)
        ).get(LoanCalculatorViewModel::class.java)

        setContent {
            val state = viewModel.state.collectAsState().value

            LoanCalculatorTheme(useDarkTheme = state.isDarkTheme) {
                LoanCalculatorScreen(viewModel = viewModel)
            }
        }
    }
}