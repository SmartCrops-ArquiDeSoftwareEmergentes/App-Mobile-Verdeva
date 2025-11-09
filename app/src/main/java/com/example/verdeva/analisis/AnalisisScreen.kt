package com.example.verdeva.analysis.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.example.verdeva.R
import com.example.verdeva.dashboard.presentation.components.BottomNavigationBar
import com.example.verdeva.dashboard.presentation.components.TopBarNutriControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.text.ParseException
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

// Mapa con nombre corto y unidad corta por sensor_id
private val SENSOR_INFO: Map<Int, Pair<String, String>> = mapOf(
    101 to Pair("Temperatura", "°C"),      // Cambiado a "°C"
    102 to Pair("Humedad", "%"),
    103 to Pair("Luz", "lx"),
    104 to Pair("Lluvia", "mm"),
    105 to Pair("pH", "pH"),
    106 to Pair("Nutrientes", "mg/L")
)

// Iconos por sensor (usa drawables existentes o genéricos)
private val SENSOR_ICONS: Map<Int, Int> = mapOf(
    101 to R.drawable.ic_field_icon,
    102 to R.drawable.ic_field_icon,
    103 to R.drawable.ic_field_icon,
    104 to R.drawable.ic_field_icon,
    105 to R.drawable.ic_field_icon,
    106 to R.drawable.ic_field_icon
)

// Colores por sensor para el acento visual
private val SENSOR_COLOR: Map<Int, Color> = mapOf(
    101 to Color(0xFFE57373), // rojo claro temperatura
    102 to Color(0xFF64B5F6), // azul humedad
    103 to Color(0xFFFFF176), // amarillo luz
    104 to Color(0xFF4FC3F7), // celeste lluvia
    105 to Color(0xFF81C784), // verde pH
    106 to Color(0xFFBA68C8)  // morado nutrientes
)

// Data class para mostrar en la UI
private data class SensorDisplay(
    val id: Int,
    val label: String,
    val formattedValue: String,
    val unit: String,
    val readableTs: String,
    val rawValue: Double
)

// Utilidad para parsear timestamps en varios formatos -> millis
private fun parseTimestampMillis(ts: String): Long {
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    )

    for (p in patterns) {
        try {
            val sdf = SimpleDateFormat(p, Locale.US)
            sdf.isLenient = false
            // Interpretar Z como UTC cuando aparezca
            if (p.contains("'Z'") || p.endsWith("Z")) {
                sdf.timeZone = TimeZone.getTimeZone("UTC")
            }
            val date = sdf.parse(ts)
            if (date != null) return date.time
        } catch (_: ParseException) {
            // Intentar siguiente patrón
        } catch (_: Exception) {
            // Ignorar y continuar
        }
    }

    // Si no pudo parsear, intentar eliminar zona final 'Z' o fracciones y reintentar simple
    return try {
        val normalized = ts.replace("Z", "")
            .split(".")[0] // elimina fracciones si existen
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        sdf.isLenient = false
        sdf.parse(normalized)?.time ?: 0L
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
    val sensorValues = remember { mutableStateListOf<SensorDisplay>() }
    var isLoading by remember { mutableStateOf(true) }

    // Historial: guardamos hasta las últimas 20 lecturas por sensor
    val sensorHistory = remember { mutableStateMapOf<Int, List<RemoteSensorReading>>() }
    var selectedSensorId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                // Hacemos polling continuo al endpoint público y actualizamos la lista
                val api = provideBeeceptorApiService()

                while (isActive) {
                    try {
                        val readings = api.getAllSensorReadings()

                        // Actualizar historial por sensor: añadir nuevas lecturas sin duplicados
                        readings.forEach { r ->
                            val prev = sensorHistory[r.sensor_id] ?: emptyList()
                            if (prev.any { it.id == r.id }) {
                                // ya existe -> skip
                            } else {
                                val combined = (prev + r)
                                    .sortedBy { parseTimestampMillis(it.timestamp) }
                                    .takeLast(20)
                                sensorHistory[r.sensor_id] = combined
                            }
                        }

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
                                // Añadimos un SensorDisplay informativo
                                sensorValues.add(SensorDisplay(-1, "Sin datos", "-", "", "-", 0.0))
                            } else {
                                // Ordenar por sensor_id para consistencia en la UI
                                latestBySensor.toSortedMap().forEach { (sensorId, reading) ->
                                    val info = SENSOR_INFO[sensorId] ?: Pair("Sensor $sensorId", "")
                                    val label = info.first
                                    val unit = info.second
                                    // Formatear el valor: entero si no tiene decimales, 1 decimal si tiene
                                    val formattedValue = if (reading.value % 1.0 == 0.0)
                                        String.format(Locale.getDefault(), "%.0f", reading.value)
                                    else
                                        String.format(Locale.getDefault(), "%.1f", reading.value)

                                    val readableTs = formatTimestampReadable(reading.timestamp)
                                    sensorValues.add(SensorDisplay(sensorId, label, formattedValue, unit, readableTs, reading.value))
                                }
                            }

                            isLoading = false
                        }
                    } catch (inner: Exception) {
                        Log.e("AnalisisScreen", "Error al obtener o procesar lecturas", inner)
                        withContext(Dispatchers.Main) {
                            // Si hay error, mostrarlo en UI reemplazando la lista
                            sensorValues.clear()
                            sensorValues.add(SensorDisplay(-1, "Error", inner.message ?: "Error desconocido", "", "", 0.0))
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
                    sensorValues.add(SensorDisplay(-1, "Error", e.message ?: "Error desconocido", "", "", 0.0))
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
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)) {
                        items(sensorValues) { sensor ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable(enabled = sensor.id != -1) { if (sensor.id != -1) selectedSensorId = sensor.id },
                                shape = RoundedCornerShape(14.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBF9))
                            ) {
                                val accent = SENSOR_COLOR[sensor.id] ?: Color(0xFFBDBDBD)
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.horizontalGradient(listOf(accent.copy(alpha = 0.08f), Color.White))
                                    )
                                    .padding(14.dp), verticalAlignment = Alignment.CenterVertically) {

                                    // Franja izquierda de color como acento
                                    Box(modifier = Modifier
                                        .width(6.dp)
                                        .height(64.dp)
                                        .background(accent.copy(alpha = 0.95f))
                                    )

                                    Spacer(modifier = Modifier.width(14.dp))

                                    // Icono
                                    val iconRes = SENSOR_ICONS[sensor.id] ?: R.drawable.ic_field_icon
                                    Icon(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = sensor.label,
                                        tint = accent,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .padding(end = 12.dp)
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = sensor.label,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp,
                                            color = Color(0xFF0B4D36)
                                        )

                                        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 6.dp)) {
                                            Text(
                                                text = sensor.formattedValue,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 24.sp,
                                                color = accent,
                                                modifier = Modifier.padding(end = 6.dp)
                                            )

                                            if (sensor.unit.isNotBlank()) {
                                                Text(
                                                    text = sensor.unit,
                                                    fontSize = 14.sp,
                                                    color = accent.copy(alpha = 0.9f),
                                                    modifier = Modifier.padding(bottom = 3.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.weight(1f))

                                            Text(
                                                text = sensor.readableTs,
                                                fontSize = 12.sp,
                                                color = Color.Gray,
                                                modifier = Modifier.padding(start = 8.dp),
                                                textAlign = TextAlign.End
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Dialog ligero para historial
    if (selectedSensorId != null) {
        val id = selectedSensorId!!
        val history = sensorHistory[id] ?: emptyList()
        val info = SENSOR_INFO[id] ?: Pair("Sensor $id", "")

        AlertDialog(
            onDismissRequest = { selectedSensorId = null },
            title = { Text(text = "Historial: ${info.first}", fontWeight = FontWeight.Bold) },
            text = {
                if (history.isEmpty()) {
                    Text("No hay historial disponible para este sensor")
                } else {
                    Column(modifier = Modifier.heightIn(max = 300.dp)) {
                        // Mostrar solo las últimas 10 entradas, ordenadas por timestamp descendente
                        val displayed = history.sortedByDescending { parseTimestampMillis(it.timestamp) }.take(10)
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(displayed) { item ->
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = formatTimestampReadable(item.timestamp), modifier = Modifier.weight(1f), color = Color.Gray)
                                    Text(text = if ((SENSOR_INFO[item.sensor_id]?.second ?: "").isNotBlank()) "${formatValue(item.value)}${SENSOR_INFO[item.sensor_id]?.second}" else formatValue(item.value), fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedSensorId = null }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

// Utilidad para formatear valores (se utiliza también en el diálogo)
private fun formatValue(value: Double): String {
    return if (value % 1.0 == 0.0) String.format(Locale.getDefault(), "%.0f", value) else String.format(Locale.getDefault(), "%.1f", value)
}
