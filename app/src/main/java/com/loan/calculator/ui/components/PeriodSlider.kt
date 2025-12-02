package com.loan.calculator.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlin.math.max

@Composable
fun PeriodSlider(
    selectedPeriod: Int,
    periods: List<Int>,
    header: String,
    onPeriodChanged: (Int) -> Unit,
    sliderColor: Color,
    minLabel: String? = null,
    maxLabel: String? = null,
    modifier: Modifier = Modifier
) {
    val currentIndex = periods.indexOf(selectedPeriod)  // Преобразование в индекс

    LoanSlider(
        value = currentIndex.toFloat(),
        onValueChange = { newIndex ->
            val period = periods[newIndex.toInt()]  // Преобразование обратно
            onPeriodChanged(period)
        },
        valueRange = 0f..(periods.size - 1).toFloat(),    // позиции
        steps = (periods.size - 2).coerceAtLeast(0),  // промежуточные шаги
        header = header,
        label = "$selectedPeriod days", // Показываем реальное значение
        sliderColor = sliderColor,
        minLabel = minLabel,
        maxLabel = maxLabel,
        modifier = modifier
    )
}
