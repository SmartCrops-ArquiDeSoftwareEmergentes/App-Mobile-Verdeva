package com.example.verdeva.membresias

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.verdeva.dashboard.presentation.components.TopBarNutriControl
import com.example.verdeva.dashboard.presentation.components.BottomNavigationBar
import com.example.verdeva.R
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembresiaScreen(
    navController: NavHostController,
    onMiPerfilClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var showPayment by remember { mutableStateOf(false) }
    var selectedPlan by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    var tarjeta by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var fv by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // Distintas fechas / datos de ejemplo
    val fechaInicio = "01/02/2025"

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            TopBarNutriControl(
                searchText = searchText,
                onSearchChange = { searchText = it },
                onMiPerfilClick = onMiPerfilClick,
                onCerrarSesionClick = onLogoutClick
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(48.dp))

                Text(
                    "Membresía",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF024728),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(24.dp))
                // ── Suscripción Básica ──
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            selectedPlan = "Básica"
                            showPayment = false
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Suscripción: Básica",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF024728)
                        )
                        Divider(
                            color = Color(0xFF024728),
                            thickness = 2.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text("• Campos Agrícolas limitados", fontSize = 14.sp)
                        Text("• Gestión limitada", fontSize = 14.sp, color = Color(0xFF024728))
                        Text("• Recomendaciones continuas", fontSize = 14.sp, color = Color(0xFF024728))
                    }
                }

                // ── Suscripción Estándar ──
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            selectedPlan = "Estándar"
                            showPayment = true
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF024728)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Suscripción: Estándar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Divider(
                            color = Color.White,
                            thickness = 2.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            "Fecha inicio: $fechaInicio",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                        )
                        Text("• Campos Agrícolas ilimitados", fontSize = 14.sp, color = Color.White)
                        Text("• Gestión ilimitada", fontSize = 14.sp, color = Color.White)
                        Text("• Recomendaciones continuas", fontSize = 14.sp, color = Color.White)
                        Text("• Registro histórico", fontSize = 14.sp, color = Color.White)
                    }
                }

                // ── Suscripción Premium ──
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            selectedPlan = "Premium"
                            showPayment = true
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Suscripción: Premium",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF024728)
                        )
                        Divider(
                            color = Color(0xFF024728),
                            thickness = 2.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            "• Campos Agrícolas ilimitados",
                            fontSize = 14.sp,
                            color = Color(0xFF024728)
                        )
                        Text("• Gestión ilimitada", fontSize = 14.sp, color = Color(0xFF024728))
                        Text("• Recomendaciones continuas", fontSize = 14.sp, color = Color(0xFF024728))
                        Text("• Registro histórico", fontSize = 14.sp, color = Color(0xFF024728))
                        Text("• Análisis Predictivo", fontSize = 14.sp, color = Color(0xFF024728))
                    }
                }
            }
        }

        // ── Diálogo de Pasarela de Pagos ──
        if (showPayment) {
            Box(
                Modifier
                    .fillMaxSize()                              // <— ocupa TODO
                    .background(Color.Black.copy(alpha = 0.2f))
                    .clickable { showPayment = false }
            )
        }

        if (showPayment) {
            Dialog(
                onDismissRequest = { showPayment = false },
                properties       = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Column(
                    Modifier
                        .widthIn(max = 340.dp)
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    // Cabecera
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showPayment = false }
                        )
                    }

                    Text(
                        "Pasarela de Pagos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF024728),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_credit_card),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(Modifier.height(16.dp))

                    // Campo tarjeta
                    OutlinedTextField(
                        value = tarjeta,
                        onValueChange = { tarjeta = it },
                        label = { Text("16 dígitos de tu tarjeta", color = Color(0xFF024728)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF024728),
                            unfocusedBorderColor = Color(0xFF024728),
                            cursorColor = Color(0xFF024728)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    // Campo email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico", color = Color(0xFF024728)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF024728),
                            unfocusedBorderColor = Color(0xFF024728),
                            cursorColor = Color(0xFF024728)
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    // F.V. y CVV
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = fv,
                            onValueChange = { fv = it },
                            label = { Text("F.V", color = Color(0xFF024728)) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF024728),
                                unfocusedBorderColor = Color(0xFF024728),
                                cursorColor = Color(0xFF024728)
                            )
                        )
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            label = { Text("CVV", color = Color(0xFF024728)) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF024728),
                                unfocusedBorderColor = Color(0xFF024728),
                                cursorColor = Color(0xFF024728)
                            )
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // — Pagar dentro de la Card —
                    val allFilled =
                        tarjeta.isNotBlank() && email.isNotBlank() && fv.isNotBlank() && cvv.isNotBlank()
                    Button(
                        onClick = {
                            if (allFilled) {
                                showPayment = false
                                showSuccess = true
                            }
                        },
                        enabled = allFilled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF024728)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(180.dp)
                            .height(48.dp)
                    ) {
                        Text("Pagar", color = Color.White)
                    }
                }
            }
        }

        // ——— Éxito Dialog ———
        if (showSuccess) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .clickable { showSuccess = false }
            )
            Dialog(onDismissRequest = { showSuccess = false }) {
                Column(
                    Modifier
                        .widthIn(max = 300.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        "¡Felicidades ahora eres miembro Premium!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF024728),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Podrás disfrutar todos los beneficios exclusivos de NutriControl, recuerda que el pago se irá realizando mes a mes hasta que canceles la suscripción.",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF024728),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { showSuccess = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF024728)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(140.dp)
                            .height(48.dp)
                    ) {
                        Text("Confirmar", color = Color.White)
                    }
                }
            }
        }
    }
}