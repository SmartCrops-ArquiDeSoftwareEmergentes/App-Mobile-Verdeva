package com.example.nutricontrol.campos

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.nutricontrol.R
import com.example.nutricontrol.dashboard.presentation.components.BottomNavigationBar
import com.example.nutricontrol.dashboard.presentation.components.TopBarNutriControl
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties
import com.example.nutricontrol.campos.components.CardCamposAgricolas
import com.example.nutricontrol.campos.components.EditCampoForm


data class CampoAgricola(
    val nombreCampo: String,
    val hectareas: Int,
    val cultivo: String,
    val estado: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CamposScreen(
    navController: NavHostController,
    onMiPerfilClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    // Lista de ejemplo
    val campos = remember {
        mutableStateListOf(
            CampoAgricola("Campo Huancayo - 1", 5, "Papa", "Activo"),
            CampoAgricola("Campo Huancayo - 2", 12, "Lechuga", "Activo"),
            CampoAgricola("Campo Huancayo - 3", 4, "Yuca", "Activo"),
            CampoAgricola("Campo Huancayo - 4", 7, "Camote", "Activo"),
            CampoAgricola("Campo Huancayo - 5", 3, "Manzana", "Activo")
        )
    }

    var editingCampo by remember { mutableStateOf<CampoAgricola?>(null) }
    var editingIndex  by remember { mutableStateOf(-1) }
    var maxHeight by remember { mutableStateOf(0.dp) }

    // Calcula una altura aproximada para que todas las cartas coincidan
    LaunchedEffect(campos) {
        val tmp = campos.maxOf {
            it.nombreCampo.length.dp +
                    "Hectáreas: ${it.hectareas}".length.dp +
                    "Cultivos: ${it.cultivo}".length.dp +
                    "Estado: ${it.estado}".length.dp
        }
        maxHeight = tmp
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            if (editingCampo == null) {
                FloatingActionButton(
                    onClick = {
                        // Preparo un nuevo campo vacío
                        editingIndex = -1
                        editingCampo = CampoAgricola("", 0, "", "")
                    },
                    containerColor = Color(0xFF024728),
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir Campo", tint = Color.White)
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)      // ← solo aquí lo aplicas
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            TopBarNutriControl(
                searchText        = searchText,
                onSearchChange    = { searchText = it },
                onMiPerfilClick   = onMiPerfilClick,
                onCerrarSesionClick = onLogoutClick
            )

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "Tus Campos Agrícolas",
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color(0xFF024728),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                if (editingCampo != null) {
                    IconButton(
                        onClick = { editingCampo = null },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF024728), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            if (editingCampo != null) {

                // Si es un campo nuevo (index == -1)
                if (editingIndex == -1) {
                    Text(
                        "Nuevo Campo:",
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = Color(0xFF024728),
                        modifier   = Modifier.fillMaxWidth().padding(start = 4.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                }

                EditCampoForm(
                    campo = editingCampo!!,
                    onSave = { updated ->
                        if (editingIndex == -1) {
                            // agregar al final
                            campos.add(updated)
                        } else {
                            // reemplazar en posición
                            campos[editingIndex] = updated
                        }
                        editingCampo = null
                    }
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        bottom = innerPadding.calculateBottomPadding()
                    )
                ) {
                    itemsIndexed(campos) { idx, campo ->
                        CardCamposAgricolas(
                            nombreCampo = campo.nombreCampo,
                            hectareas   = campo.hectareas,
                            cultivo     = campo.cultivo,
                            estado      = campo.estado,
                            onEdit      = {
                                editingIndex = idx
                                editingCampo = campo
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .heightIn(min = maxHeight)
                        )
                    }
                }
            }
        }
    }
}