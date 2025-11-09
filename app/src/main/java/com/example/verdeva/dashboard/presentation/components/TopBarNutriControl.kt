package com.example.verdeva.dashboard.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt

@Composable
fun TopBarNutriControl(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onMiPerfilClick: () -> Unit,
    onCerrarSesionClick: () -> Unit
) {
    val iconColor = Color(0xFF024728)
    var showNotificationsPopup by remember { mutableStateOf(false) }
    var showSettingsPopup by remember { mutableStateOf(false) }
    var notifOffset by remember { mutableStateOf(Offset.Zero) }
    var settingsOffset by remember { mutableStateOf(Offset.Zero) }

    val density = LocalDensity.current

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchChange,
                placeholder = { Text("Buscar en NutriControl") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar", tint = iconColor)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = iconColor,
                    unfocusedBorderColor = iconColor
                )
            )

            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    showNotificationsPopup = !showNotificationsPopup
                    showSettingsPopup = false
                },
                modifier = Modifier.onGloballyPositioned {
                    val position = it.localToWindow(Offset.Zero)
                    notifOffset = position
                }
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = iconColor)
            }
            IconButton(
                onClick = {
                    showSettingsPopup = !showSettingsPopup
                    showNotificationsPopup = false
                },
                modifier = Modifier.onGloballyPositioned {
                    val position = it.localToWindow(Offset.Zero)
                    settingsOffset = position
                }
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Configuración", tint = iconColor)
            }
        }

        if (showNotificationsPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showNotificationsPopup = false }
            )

            Popup(
                offset = with(density) {
                    IntOffset(
                        notifOffset.x.roundToInt(),
                        notifOffset.y.roundToInt() + 0.dp.roundToPx()
                    )
                },
                properties = PopupProperties(focusable = true)
            ) {
                AnimatedVisibility(
                    visible = showNotificationsPopup,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .width(280.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .background(Color(0xFF024728), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { showNotificationsPopup = false }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                                }
                            }
                            Text(
                                text = "• Activa el actuador de Riego ahora mismo para las Papas",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = Color.White.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Evitar exceso de riego si hay lluvias acumuladas",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        if (showSettingsPopup) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { showSettingsPopup = false }
            )

            Popup(
                offset = with(density) {
                    IntOffset(
                        settingsOffset.x.roundToInt(),
                        settingsOffset.y.roundToInt() + 0.dp.roundToPx()
                    )
                },
                properties = PopupProperties(focusable = true)
            ) {
                AnimatedVisibility(
                    visible = showSettingsPopup,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .background(Color(0xFF024728), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { showSettingsPopup = false }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                                }
                            }
                            Text(
                                "Mi Perfil",
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showSettingsPopup = false
                                        onMiPerfilClick()
                                    }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = Color.White.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Cerrar sesión",
                                fontSize = 14.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showSettingsPopup = false
                                        onCerrarSesionClick()
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}