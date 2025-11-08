package com.example.nutricontrol.dashboard.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.ceil
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarioRecomendacion(modifier: Modifier = Modifier) {
    val today = LocalDate.now()
    val currentMonth = today.month
    val currentYear = today.year
    val primerDiaMes = LocalDate.of(currentYear, currentMonth, 1)
    val diasDelMes = currentMonth.length(primerDiaMes.isLeapYear)
    val primerDiaSemana = primerDiaMes.dayOfWeek.value % 7
    val totalCeldas = ceil((diasDelMes + primerDiaSemana) / 7.0).toInt() * 7
    val dias = (1 - primerDiaSemana until 1 - primerDiaSemana + totalCeldas)

    val diaColor = Color(0xFFAD822F)
    val fondoCalendario = Color(0xFFF5F5F5)

    Card(
        modifier = modifier.width(210.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(fondoCalendario)
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "${currentMonth.getDisplayName(TextStyle.FULL, Locale("es")).replaceFirstChar { it.uppercase() }} $currentYear",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0A3A27)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf( "Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa").forEach {
                        Text(
                            text = it,
                            modifier = Modifier.width(16.dp),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0A3A27)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                dias.chunked(7).forEach { semana ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        semana.forEach { dia ->
                            val fecha = primerDiaMes.plusDays((dia - 1).toLong())
                            val esDelMesActual = dia in 1..diasDelMes
                            val esHoy = esDelMesActual && dia == today.dayOfMonth

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        color = if (esHoy) Color(0xFF0A3A27) else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (dia > 0 && fecha.month == currentMonth) dia.toString() else "",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = if (esHoy) Color.White else diaColor
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recomendación del día:",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0A3A27)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    "•",
                    color = Color(0xFF0A3A27),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = "Activa el actuador de Riego ahora mismo",
                    color = Color(0xFF0A3A27),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}