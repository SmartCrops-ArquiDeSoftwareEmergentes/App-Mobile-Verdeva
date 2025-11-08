package com.example.nutricontrol.campos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.nutricontrol.R

@Composable
fun CardCamposAgricolas(
    nombreCampo: String,
    hectareas: Int,
    cultivo: String,
    estado: String,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) } // Para manejar la visibilidad del menú desplegable
    var showDialog by remember { mutableStateOf(false) }
    // Creamos una tarjeta con un fondo con imagen de campo agrícola
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp), // Esquinas redondeadas
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) // Fondo suave
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cabecera con ícono de editar en la parte superior derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // Botón circular de editar
                IconButton(
                    onClick = { expanded = true }, // Al hacer clic, expandir el menú
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF024728), shape = CircleShape) // Botón verde y circular
                        .padding(6.dp) // Apply padding *inside* the background
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar Campo",
                        tint = Color.White // Ícono en blanco
                    )
                }

                // Menú desplegable con opciones
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color(0xFF024728))
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    // Opción Eliminar
                    DropdownMenuItem(
                        text = { Text("Eliminar", color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }, // Corrected: Pass a Composable lambda for text
                        onClick = {
                            /* Lógica para eliminar */
                            showDialog = true
                            expanded = false // Cerrar el menú
                        },
                        //modifier = Modifier.background(Color(0xFF024728))
                    )

                    Divider(
                        color = Color.White,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.5f) // Solo la mitad del ancho
                            .align(Alignment.CenterHorizontally) // Centrar la línea
                            .padding(vertical = 8.dp) // Espaciado vertical
                    )

                    // Opción Editar
                    DropdownMenuItem(
                        text = { Text("Editar", color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }, // Corrected: Pass a Composable lambda for text
                        onClick = {
                            onEdit()
                        },
                        //modifier = Modifier.background(Color(0xFF024728))
                    )
                }
            }

            // Icono representativo del campo agrícola
            Image(
                painter = painterResource(id = R.drawable.ic_field_icon), // Usa tu propia imagen de campo agrícola
                contentDescription = "Icono de campo agrícola",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espacio entre icono y texto

            // Información del campo agrícola, con el color de texto verde
            Text(text = nombreCampo, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF024728))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Hectáreas: $hectareas", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF024728))  // Cambio aquí
            Text(text = "Cultivos: $cultivo", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF024728))      // Cambio aquí
            Text(text = "Estado: $estado", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF024728))        // Cambio aquí
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)) // scrim semitransparente
                    .clickable { showDialog = false }, // cerrar al click fuera
                contentAlignment = Alignment.Center
            ) {
                Box { // contenedor del diálogo
                    Column(
                        modifier = Modifier
                            .widthIn(max = 320.dp)
                            .background(Color.White, shape = RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        // botón cerrar (X)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { showDialog = false }
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        // Título
                        Text(
                            text = "¿Deseas eliminar el $nombreCampo?",
                            color = Color(0xFF024728),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(12.dp))

                        // Cuerpo
                        Text(
                            text = "Ten en cuenta que los cultivos y dispositivos conectados se eliminarán\n(Esta acción no puede deshacerse).",
                            color = Color(0xFF024728),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(48.dp)) // espacio para el botón
                    }

                    // Botón central “Eliminar” que sobresale
                    Button(
                        onClick = {
                            /* Lógica para eliminar */
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF024728)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 16.dp) // sobresalir del contenedor
                            .width(200.dp)    // ancho fijo
                            .height(56.dp)    // alto más grande
                    ) {
                        Text("Eliminar",
                            color = Color.White,
                            fontSize = 18.sp,  // texto más grande
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}