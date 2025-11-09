package com.example.verdeva.dashboard.presentation.components.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IndicadorCircularPorcentaje(
    porcentaje: Int,
    diametro: Dp = 64.dp,
    color: Color = Color(0xFFAF832D),
    colorFondo: Color = Color(0xFFE6E6E6)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(diametro)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = 360 * (porcentaje / 100f)
            val strokeWidth = size.minDimension * 0.1f

            drawArc(
                color = colorFondo,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(size.width, size.height),
                topLeft = Offset.Zero
            )

            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(size.width, size.height),
                topLeft = Offset.Zero
            )
        }

        Text(
            text = "$porcentaje%",
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}