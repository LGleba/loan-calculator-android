package com.loan.calculator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun LoanSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    header: String,
    label: String,
    sliderColor: Color,
    minLabel: String? = null,
    maxLabel: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header: Вопрос и значение
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = header,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = label,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Кастомный слайдер
        CustomSliderCanvas(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            valueRange = valueRange,
            steps = steps,
            activeColor = sliderColor,
            inactiveColor = Color(0xFFE8E8E8),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 8.dp)
        )

        // Вычисление текста для Min и Max
        val leftText = minLabel ?: formatSliderValue(valueRange.start)
        val rightText = maxLabel ?: formatSliderValue(valueRange.endInclusive)

        // Min and Max values
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Text(
                text = leftText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp
                ),
                color = Color(0xFF7C7C7C)
            )

            Text(
                text = rightText,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                ),
                color = Color(0xFF7C7C7C)
            )
        }
    }
}

@Composable
private fun CustomSliderCanvas(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newValue = calculateValueFromPosition(
                        position = offset.x,
                        width = size.width.toFloat(),
                        valueRange = valueRange,
                        steps = steps
                    )
                    onValueChange(newValue)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val newValue = calculateValueFromPosition(
                        position = change.position.x,
                        width = size.width.toFloat(),
                        valueRange = valueRange,
                        steps = steps
                    )
                    onValueChange(newValue)
                }
            }
    ) {
        val trackHeight = 16.dp.toPx()
        val thumbRadius = 28.dp.toPx()
        val trackY = size.height / 2

        // Вычисляем позицию thumb'а
        val valuePercent = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
        val thumbX = (size.width * valuePercent).coerceIn(thumbRadius, size.width - thumbRadius)

        // Рисуем трек (неактивная часть)
        drawRoundRect(
            color = inactiveColor,
            topLeft = Offset(0f, trackY - trackHeight / 2),
            size = androidx.compose.ui.geometry.Size(size.width, trackHeight),
            cornerRadius = CornerRadius(trackHeight / 2, trackHeight / 2)
        )

        // Рисуем активную часть трека с закругленными краями
        drawRoundRect(
            color = activeColor,
            topLeft = Offset(0f, trackY - trackHeight / 2),
            size = androidx.compose.ui.geometry.Size(thumbX, trackHeight),
            cornerRadius = CornerRadius(trackHeight / 2, trackHeight / 2)
        )

        // Рисуем thumb с градиентом и тенью
        drawThumb(
            center = Offset(thumbX, trackY),
            radius = thumbRadius,
            color = activeColor
        )
    }
}

private fun DrawScope.drawThumb(
    center: Offset,
    radius: Float,
    color: Color
) {
    // Тень
    drawCircle(
        color = Color.Black.copy(alpha = 0.15f),
        radius = radius,
        center = Offset(center.x, center.y + 4.dp.toPx())
    )

    // Внешний круг с градиентом
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color.copy(alpha = 0.9f),
                color
            ),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
            radius = radius
        ),
        radius = radius,
        center = center
    )

    // Внутренний светлый блик
    drawCircle(
        color = Color.White.copy(alpha = 0.3f),
        radius = radius * 0.4f,
        center = Offset(center.x - radius * 0.25f, center.y - radius * 0.25f)
    )
}

private fun calculateValueFromPosition(
    position: Float,
    width: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
): Float {
    val percent = (position / width).coerceIn(0f, 1f)
    val rawValue = valueRange.start + (percent * (valueRange.endInclusive - valueRange.start))

    return if (steps > 0) {
        val stepSize = (valueRange.endInclusive - valueRange.start) / (steps + 1)
        val nearestStep = ((rawValue - valueRange.start) / stepSize).roundToInt()
        (valueRange.start + (nearestStep * stepSize)).coerceIn(valueRange.start, valueRange.endInclusive)
    } else {
        rawValue.coerceIn(valueRange.start, valueRange.endInclusive)
    }
}

private fun formatSliderValue(value: Float): String {
    return when {
        value >= 1_000_000 -> {
            val millions = (value / 1_000_000).toInt()
            val thousands = ((value % 1_000_000) / 1_000).toInt()
            String.format("%d %03d 000", millions, thousands)
        }
        value >= 1_000 -> {
            val thousands = (value / 1_000).toInt()
            val remainder = (value % 1_000).toInt()
            if (remainder == 0) {
                String.format("%d 000", thousands)
            } else {
                String.format("%d %03d", thousands, remainder)
            }
        }
        else -> value.toInt().toString()
    }
}
