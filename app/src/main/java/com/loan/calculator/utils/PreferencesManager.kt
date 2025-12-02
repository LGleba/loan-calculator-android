package com.loan.calculator.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("loan_calculator_prefs", Context.MODE_PRIVATE)

    fun saveLoanAmount(amount: Int) {
        prefs.edit().putInt("loan_amount", amount).apply()
    }

    fun getLoanAmount(): Int = prefs.getInt("loan_amount", 10000)

    fun saveLoanPeriod(period: Int) {
        prefs.edit().putInt("loan_period", period).apply()
    }

    fun getLoanPeriod(): Int = prefs.getInt("loan_period", 14)

    fun saveThemePreference(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_theme", isDark).apply()
    }

    fun getThemePreference(): Boolean = prefs.getBoolean("is_dark_theme", false)

    fun clear() {
        prefs.edit().clear().apply()
    }
}