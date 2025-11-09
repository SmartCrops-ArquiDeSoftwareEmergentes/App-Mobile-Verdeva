package com.example.verdeva.analysis.presentation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import com.example.verdeva.dashboard.presentation.components.BottomNavigationBar
import com.example.verdeva.dashboard.presentation.components.TopBarNutriControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import okhttp3.OkHttpClient
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.verdeva.dataStore

val jwtTokenKey = stringPreferencesKey("jwt_token")
val usernameKey = stringPreferencesKey("username")

suspend fun getJwtToken(context: android.content.Context): String? {
    return context.dataStore.data.first()[jwtTokenKey]
}

suspend fun getUsername(context: android.content.Context): String? {
    return context.dataStore.data.first()[usernameKey]
}

// ==== Modelos ====

data class Sensor(
    val id: Int,
    val type: String,
    val unitOfMeasurement: String,
    val status: String
)

data class SensorReading(
    val timestamp: String,
    val value: Double
)

// ==== API ====

interface SensorApiService {
    @GET("api/Device/sensors/by-user/{username}")
    suspend fun getSensorsByUser(
        @Path("username") username: String,
        @Header("Authorization") authHeader: String
    ): List<Sensor>

    @GET("api/Device/sensors/{sensorId}/readings")
    suspend fun getSensorReadings(
        @Path("sensorId") sensorId: Int,
        @Header("Authorization") authHeader: String
    ): List<SensorReading>
}

fun provideSensorApiService(): SensorApiService {
    val client = OkHttpClient.Builder().build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://nutricontrolapifilesadministration-bvf4bbbpgpb5h5dw.brazilsouth-01.azurewebsites.net/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(SensorApiService::class.java)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalisisScreen(
    navController: NavHostController,
    onMiPerfilClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var searchText by remember { mutableStateOf("") }
    val sensorValues = remember { mutableStateListOf<Pair<String, String>>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val jwt = getJwtToken(context)
                val username = getUsername(context)

                Log.d("AnalisisScreen", "JWT: $jwt")
                Log.d("AnalisisScreen", "Username: $username")

                if (jwt != null && username != null) {
                    val authHeader = "Bearer $jwt"
                    val api = provideSensorApiService()

                    val sensors = api.getSensorsByUser(username, authHeader)
                    Log.d("AnalisisScreen", "Sensores encontrados: ${sensors.size}")

                    sensorValues.clear()
                    for (sensor in sensors) {
                        val readings = api.getSensorReadings(sensor.id, authHeader)
                        val latestValue = readings.lastOrNull()?.value
                        if (latestValue != null) {
                            sensorValues.add(sensor.type to "$latestValue ${sensor.unitOfMeasurement}")
                        }
                    }
                } else {
                    Log.d("AnalisisScreen", "JWT o username no encontrados en DataStore.")
                }
            } catch (e: Exception) {
                Log.e("AnalisisScreen", "Error al obtener sensores o lecturas", e)
            } finally {
                isLoading = false
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
