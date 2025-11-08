package com.example.nutricontrol.dashboard.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoAguaAhorrada(
    valores: List<Int>,
    animar: Boolean = true,
    modifier: Modifier = Modifier
) {
    val max = valores.maxOrNull() ?: 1
    val maxBarHeight = 120.dp

    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Agua ahorrada (Litros)",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0A3A27)
            )
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                valores.forEachIndexed { index, valor ->
                    val targetHeight = if (animar) (valor.toFloat() / max) * maxBarHeight.value else 0f
                    val animatedHeight by animateDpAsState(
                        targetValue = Dp(targetHeight),
                        animationSpec = tween(durationMillis = 600),
                        label = "barHeight"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = valor.toString(),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0A3A27)
                        )
                        Box(
                            modifier = Modifier
                                .height(animatedHeight)
                                .width(20.dp)
                                .background(
                                    color = if (valor == max) Color(0xFFAD822F) else Color(0xFF0A3A27),
                                )
                        )
                        Canvas(modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)) {
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 2.5f
                            )
                        }
                        Text(
                            text = dias[index],
                            fontSize = 11.sp,
                            maxLines = 1,
                            softWrap = false,
                            color = Color(0xFF0A3A27)
                        )
                    }
                }
            }
        }
    }
}