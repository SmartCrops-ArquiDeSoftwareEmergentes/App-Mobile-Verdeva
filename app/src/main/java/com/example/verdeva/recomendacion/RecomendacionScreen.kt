package com.example.verdeva.recomendacion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.verdeva.dashboard.presentation.components.BottomNavigationBar
import com.example.verdeva.dashboard.presentation.components.TopBarNutriControl
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecomendacionScreen(
    navController: NavHostController,
    onMiPerfilClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    // Fecha
    val fechaHoy = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            TopBarNutriControl(
                searchText        = searchText,
                onSearchChange    = { searchText = it },
                onMiPerfilClick   = onMiPerfilClick,
                onCerrarSesionClick = onLogoutClick
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Recomendaciones de tus",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF024728),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Cultivos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFAD822F),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Fecha: $fechaHoy",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF024728),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Dos cards lado a lado
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RecomendacionCard(
                    title = "Cultivos de Papas",
                    bullets = listOf(
                        "Activar riego por goteo si la humedad del suelo está por debajo del 60%",
                        "Ajustar pH del suelo si es menor a 5.5",
                        "Aplicar control preventivo contra tizón tardío si la humedad relativa es alta (>85%)"
                    ),
                    modifier = Modifier
                        .weight(1f)                // <— aquí sí puedes usar weight
                        .height(450.dp)
                )

                RecomendacionCard(
                    title = "Cultivos de Camote",
                    bullets = listOf(
                        "Evitar exceso de riego si hay lluvias acumuladas",
                        "Verificar niveles de potasio durante fase de engrosamiento de raíces",
                        "Realizar deshierbe cada 20 días"
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(450.dp)
                )
            }
        }
    }

}

