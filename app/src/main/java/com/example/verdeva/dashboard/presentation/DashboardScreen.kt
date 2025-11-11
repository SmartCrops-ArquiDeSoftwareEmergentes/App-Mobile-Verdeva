package com.example.verdeva.dashboard.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.verdeva.dashboard.presentation.components.*
import com.example.verdeva.iam.presentation.sign_in.TokenManager
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    userName: String = "Hernan Morales",
    onMiPerfilClick: () -> Unit,
    onCerrarSesionClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val animTrigger = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    var storedUsername by remember { mutableStateOf(userName) }

    // Lista aleatoria para gráfico de agua ahorrada
    var randomWaterData by remember { mutableStateOf(listOf<Int>()) }

    LaunchedEffect(Unit) {
        animTrigger.value = false
        animTrigger.value = true

        val loadedUsername = tokenManager.getUsername()
        if (!loadedUsername.isNullOrBlank()) {
            storedUsername = loadedUsername
        }

        // Generar 7 valores aleatorios entre 1 y 10
        randomWaterData = List(7) { Random.nextInt(1, 11) }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))

                TopBarNutriControl(
                    searchText,
                    onSearchChange = { searchText = it },
                    onMiPerfilClick = onMiPerfilClick,
                    onCerrarSesionClick = {
                        navController.navigate("sign_in") {
                            popUpTo("sign_in") { inclusive = true }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Bienvenido a Verdeva,",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF024728),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = storedUsername,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFAD8126),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CardCamposAgricolas(activos = 5, inactivos = 1, modifier = Modifier.weight(1f))
                    CardDispositivos(cantidad = 8, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))
                CardTotalCultivos(total = 10, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                GraficoAguaAhorrada(
                    valores = randomWaterData,
                    animar = animTrigger.value
                )

                Spacer(modifier = Modifier.height(16.dp))
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)
                ) {
                    val maxHeight = this.maxHeight

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CalendarioRecomendacion(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(210.dp)
                        )
                        GraficoCrecimientoCultivos(
                            datos = listOf(
                                "Tom" to 30,
                                "Lech" to 21,
                                "Man" to 25,
                                "Pera" to 21,
                                "Plat" to 10,
                                "Yuc" to 20,
                                "Uva" to 8,
                                "Pla" to 18
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
