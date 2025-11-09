package com.example.verdeva.dashboard.presentation.components.shared

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IndicadorCircular(
    porcentaje: Int,
    titulo: String,
    color: Color = Color(0xFFAF832D),
    radio: Dp = 60.dp,
    grosor: Dp = 10.dp,
    unidad: String = "%"
) {
    val porcentajeFloat = porcentaje.toFloat() / 100f

    Box(
        modifier = Modifier
            .size(radio * 2)
            .padding(4.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = grosor.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * porcentajeFloat,
                useCenter = false,
                style = Stroke(width = grosor.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Text(
                text = "$porcentaje$unidad",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}