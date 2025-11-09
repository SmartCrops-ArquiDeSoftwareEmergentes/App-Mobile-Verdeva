package com.example.verdeva.analysis.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.example.verdeva.dashboard.presentation.components.BottomNavigationBar
import com.example.verdeva.dashboard.presentation.components.TopBarNutriControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import java.text.SimpleDateFormat
import java.util.*

// ==== Modelos y API (nuevo endpoint público Beeceptor) ====

// Modelo que coincide con la respuesta que enviaste
data class RemoteSensorReading(
    val sensor_id: Int,
    val value: Double,
    val created_user: Int?,
    val timestamp: String,
    val id: String
)

interface BeeceptorApiService {
    @GET("api/sensor-readings")
    suspend fun getAllSensorReadings(): List<RemoteSensorReading>
}

fun provideBeeceptorApiService(): BeeceptorApiService {
    val client = OkHttpClient.Builder().build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://verdeva-sensors.free.beeceptor.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(BeeceptorApiService::class.java)
}

// Utilidad para parsear timestamps ISO (ej: "2025-10-29T21:37:00") -> millis
private fun parseTimestampMillis(ts: String): Long {
    return try {
        // Creamos una instancia local porque SimpleDateFormat no es thread-safe
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        sdf.isLenient = false
        sdf.parse(ts)?.time ?: 0L
    } catch (e: Exception) {
        Log.w("AnalisisScreen", "parseTimestampMillis: no se pudo parsear timestamp: $ts", e)
        0L
    }
}

// Formatea un timestamp ISO a una cadena legible, p. ej. "29/10 21:37:00".
private fun formatTimestampReadable(ts: String): String {
    return try {
        val millis = parseTimestampMillis(ts)
        if (millis <= 0L) return "-"
        val out = SimpleDateFormat("dd/MM HH:mm:ss", Locale.getDefault())
        out.format(Date(millis))
    } catch (e: Exception) {
        Log.w("AnalisisScreen", "formatTimestampReadable: fallo al formatear timestamp: $ts", e)
        "-"
    }
}

@Composable
fun AnalisisScreen(
    navController: NavHostController,
    onMiPerfilClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    val sensorValues = remember { mutableStateListOf<Pair<String, String>>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                // Hacemos polling continuo al endpoint público y actualizamos la lista
                val api = provideBeeceptorApiService()

                while (isActive) {
                    try {
                        val readings = api.getAllSensorReadings()

                        // Agrupar por sensor_id y elegir la lectura con timestamp más reciente
                        val latestBySensor: Map<Int, RemoteSensorReading> = readings
                            .groupBy { it.sensor_id }
                            .mapValues { entry ->
                                entry.value.maxByOrNull { r ->
                                    parseTimestampMillis(r.timestamp)
                                }!!
                            }

                        // Actualizar la lista de valores en el hilo principal
                        withContext(Dispatchers.Main) {
                            sensorValues.clear()

                            if (latestBySensor.isEmpty()) {
                                // Mostrar mensaje vacío o placeholder
                                sensorValues.add("Sin datos" to "No se encontraron lecturas")
                            } else {
                                // Ordenar por sensor_id para consistencia en la UI
                                latestBySensor.toSortedMap().forEach { (sensorId, reading) ->
                                    val label = "Sensor $sensorId"
                                    val readableTs = formatTimestampReadable(reading.timestamp)
                                    val valueText = "${reading.value} · $readableTs"
                                    sensorValues.add(label to valueText)
                                }
                            }

                            isLoading = false
                        }
                    } catch (inner: Exception) {
                        Log.e("AnalisisScreen", "Error al obtener o procesar lecturas", inner)
                        withContext(Dispatchers.Main) {
                            // Si hay error, mostrarlo en UI reemplazando la lista
                            sensorValues.clear()
                            sensorValues.add("Error" to (inner.message ?: "Error desconocido"))
                            isLoading = false
                        }
                    }

                    // Esperar antes del siguiente polling
                    delay(5000)
                }
            } catch (e: Exception) {
                Log.e("AnalisisScreen", "Error en polling de lecturas", e)
                withContext(Dispatchers.Main) {
                    sensorValues.clear()
                    sensorValues.add("Error" to (e.message ?: "Error desconocido"))
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))

                TopBarNutriControl(
                    searchText = searchText,
                    onSearchChange = { searchText = it },
                    onMiPerfilClick = onMiPerfilClick,
                    onCerrarSesionClick = onLogoutClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Lecturas en tiempo real",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF024728),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF024728))
                    }
                } else {
                    sensorValues.forEach { (type, value) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F5))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = type,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF024728)
                                )
                                Text(
                                    text = value,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
