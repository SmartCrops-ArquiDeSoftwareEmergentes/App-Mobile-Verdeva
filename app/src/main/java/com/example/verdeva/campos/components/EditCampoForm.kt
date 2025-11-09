package com.example.verdeva.campos.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verdeva.R
import com.example.verdeva.campos.CampoAgricola

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditCampoForm(
    campo: CampoAgricola,
    onSave: (CampoAgricola) -> Unit
) {
    var nombre    by remember { mutableStateOf(campo.nombreCampo) }
    var hectareas by remember { mutableStateOf(campo.hectareas.toString()) }
    var estado    by remember { mutableStateOf(campo.estado) }

    // chequear que todos los campos tengan algo
    val isFormValid = nombre.isNotBlank()
            && hectareas.isNotBlank()
            && estado.isNotBlank()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(16.dp))

        Card(
            shape     = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors    = CardDefaults.cardColors(containerColor = Color.White),
            modifier  = Modifier
                .widthIn(max = 320.dp)
                .padding(horizontal = 8.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Image(
                    painter           = painterResource(id = R.drawable.ic_field_icon),
                    contentDescription = null,
                    modifier           = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value          = nombre,
                    onValueChange  = { nombre = it },
                    label          = { Text("Nombre:", fontSize = 16.sp, color = Color(0xFF024728))},
                    textStyle      = TextStyle(fontSize = 18.sp, color = Color(0xFF024728)),
                    singleLine     = true,
                    modifier       = Modifier.fillMaxWidth(),
                    colors         = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor   = Color(0xFF024728),
                        unfocusedBorderColor = Color(0xFF024728),
                        cursorColor          = Color(0xFF024728)
                    )
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value             = hectareas,
                    onValueChange     = { hectareas = it.filter(Char::isDigit) },
                    label             = { Text("Hectáreas:", fontSize = 16.sp, color = Color(0xFF024728)) },
                    textStyle         = TextStyle(fontSize = 18.sp, color = Color(0xFF024728)),
                    singleLine        = true,
                    keyboardOptions   = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier          = Modifier.fillMaxWidth(),
                    colors            = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor   = Color(0xFF024728),
                        unfocusedBorderColor = Color(0xFF024728),
                        cursorColor          = Color(0xFF024728)
                    )
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value          = estado,
                    onValueChange  = { estado = it },
                    label          = { Text("Estado:", fontSize = 16.sp, color = Color(0xFF024728)) },
                    textStyle      = TextStyle(fontSize = 18.sp, color = Color(0xFF024728)),
                    singleLine     = true,
                    modifier       = Modifier.fillMaxWidth(),
                    colors         = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor   = Color(0xFF024728),
                        unfocusedBorderColor = Color(0xFF024728),
                        cursorColor          = Color(0xFF024728)
                    )
                )

                Spacer(Modifier.height(32.dp))
            }
        }

        FloatingActionButton(
            onClick = {
                if (isFormValid) {
                    val nuevaHect = hectareas.toIntOrNull() ?: campo.hectareas
                    onSave(campo.copy(
                        nombreCampo = nombre,
                        hectareas    = nuevaHect,
                        estado       = estado
                    ))
                }
            },
            containerColor = if (isFormValid) Color(0xFF024728) else Color.Gray,
            shape          = CircleShape,
            modifier       = Modifier
                .offset(y = (-28).dp)
                .size(64.dp)
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Guardar",
                tint = if (isFormValid) Color.White else Color.LightGray,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}