package com.loan.calculator.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    fun formatDate(date: LocalDate, locale: Locale = Locale.US): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", locale)
        return date.format(formatter)
    }

    fun getRepaymentDaysLeft(date: LocalDate): Long {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), date)
    }
}