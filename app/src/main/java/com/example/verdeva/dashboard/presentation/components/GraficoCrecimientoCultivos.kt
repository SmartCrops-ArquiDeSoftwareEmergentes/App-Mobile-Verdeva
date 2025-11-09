package com.example.verdeva.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoCrecimientoCultivos(
    datos: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    val maxValor = datos.maxOfOrNull { it.second } ?: 1

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Crecimiento de cultivos",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0A3A27)
            )

            Spacer(modifier = Modifier.height(8.dp))

            datos.forEach { (nombre, valor) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = nombre,
                        modifier = Modifier.width(40.dp),
                        fontSize = 12.sp,
                        color = Color(0xFF0A3A27)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(18.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(valor / maxValor.toFloat())
                                .fillMaxHeight()
                                .background(
                                    if (valor == maxValor) Color(0xFFAD822F) else Color(0xFF0A3A27),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "+${valor}%",
                        fontSize = 12.sp,
                        color = Color(0xFF0A3A27)
                    )
                }
            }
        }
    }
}