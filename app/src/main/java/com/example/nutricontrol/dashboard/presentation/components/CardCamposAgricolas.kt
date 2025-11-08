package com.example.nutricontrol.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import com.example.nutricontrol.dashboard.presentation.components.shared.IndicadorCircularPorcentaje

@Composable
fun CardCamposAgricolas(activos: Int, inactivos: Int, modifier: Modifier = Modifier) {
    val total = activos + inactivos
    val porcentajeActivo = if (total != 0) activos * 100f / total else 0f
    val sweepActivo = (360f * porcentajeActivo) / 100f

    Card(
        modifier = modifier
            .height(230.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Campos Agrícolas",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF024728)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Canvas(modifier = Modifier.size(90.dp)) {
                drawArc(
                    color = Color(0xFFAF832D),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 50f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color(0xFF024728),
                    startAngle = -90f,
                    sweepAngle = sweepActivo,
                    useCenter = false,
                    style = Stroke(width = 50f, cap = StrokeCap.Round)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF024728), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Campos Activos", fontSize = 12.sp, color = Color(0xFF024728))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$activos", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF024728))
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFAF832D), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Campos Inactivos", fontSize = 12.sp, color = Color(0xFF024728))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("$inactivos", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF024728))
                }
            }
        }
    }
}