package com.loan.calculator.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object NumberFormatter {
    fun formatCurrency(amount: Double): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val format = DecimalFormat("#,##0.##", symbols)
        return "$${format.format(amount)}"
    }

    fun formatAmount(amount: Int): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        val format = DecimalFormat("#,##0", symbols)
        return format.format(amount)
    }

    fun formatPercent(percent: Double): String {
        return String.format("%.1f%%", percent)
    }
}