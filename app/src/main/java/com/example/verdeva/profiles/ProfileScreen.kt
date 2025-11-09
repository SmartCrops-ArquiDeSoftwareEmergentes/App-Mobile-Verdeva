package com.example.verdeva.profiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verdeva.dashboard.presentation.components.TopBarNutriControl

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userInitial: String = "H",
    nombre: String = "Hernan Emilio",
    apellido: String = "Morales Calderón",
    fechaCreacion: String = "10/05/2025",
    suscripcion: String = "ESTÁNDAR",
    correo: String = "ernan@gmail.com",
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onMiPerfilClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            TopBarNutriControl(
                searchText = searchText,
                onSearchChange = { searchText = it },
                onMiPerfilClick = onMiPerfilClick,
                onCerrarSesionClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mi Perfil",
                fontSize = 24.sp,
                color = Color(0xFF024728),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Box(contentAlignment = Alignment.TopEnd) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF024728)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userInitial,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .offset(x = (-8).dp, y = 8.dp)
                        .size(30.dp)
                        .background(Color(0xFFAF832D), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileItem("Nombres:", nombre)
                    ProfileItem("Apellidos:", apellido)
                    ProfileItem("Creación de cuenta:", fechaCreacion)
                    ProfileItem("Suscripción:", suscripcion)
                    ProfileItem("Correo electrónico:", correo)

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(
                            onClick = onEditClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAF832D)),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Editar Perfil", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            }



            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .width(160.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF024728)),
                shape = RoundedCornerShape(50)
            ) {
                Text("Cerrar sesión", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Text(
        text = buildString {
            append(label)
            append(" ")
            append(value)
        },
        fontSize = 15.sp,
        fontWeight = if (label.endsWith(":")) FontWeight.Bold else FontWeight.Normal,
        color = if (label.endsWith(":")) Color(0xFF024728) else Color.Black,
        modifier = Modifier.padding(vertical = 4.dp),
        textAlign = TextAlign.Start
    )
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userInitial: String = "H",
    nombreInicial: String = "Hernan Emilio",
    apellidoInicial: String = "Morales Calderón",
    fechaCreacion: String = "10/05/2025",
    suscripcion: String = "ESTÁNDAR",
    correoInicial: String = "ernan@gmail.com",
    onLogoutClick: () -> Unit,
    onMiPerfilClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf(nombreInicial) }
    var apellido by remember { mutableStateOf(apellidoInicial) }
    var correo by remember { mutableStateOf(correoInicial) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            TopBarNutriControl(
                searchText = searchText,
                onSearchChange = { searchText = it },
                onMiPerfilClick = onMiPerfilClick,
                onCerrarSesionClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Mi Perfil",
                fontSize = 24.sp,
                color = Color(0xFF024728),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(contentAlignment = Alignment.TopEnd) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF024728)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userInitial,
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                if (!isEditing) {
                    IconButton(
                        onClick = { isEditing = true },
                        modifier = Modifier
                            .offset(x = (-8).dp, y = 8.dp)
                            .size(30.dp)
                            .background(Color(0xFFAF832D), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isEditing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = apellido,
                            onValueChange = { apellido = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { isEditing = false },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF024728)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("✓", color = Color.White, fontSize = 24.sp)
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ProfileItem("Nombres:", nombre)
                        ProfileItem("Apellidos:", apellido)
                        ProfileItem("Creación de cuenta:", fechaCreacion)
                        ProfileItem("Suscripción:", suscripcion)
                        ProfileItem("Correo electrónico:", correo)

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Button(
                                onClick = { isEditing = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAF832D)),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Editar Perfil", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .align(Alignment.End)
                        .width(160.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF024728)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Cerrar sesión", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Text(
        text = buildString {
            append(label)
            append(" ")
            append(value)
        },
        fontSize = 15.sp,
        fontWeight = if (label.endsWith(":")) FontWeight.Bold else FontWeight.Normal,
        color = if (label.endsWith(":")) Color(0xFF024728) else Color.Black,
        modifier = Modifier.padding(vertical = 4.dp),
        textAlign = TextAlign.Start
    )
}