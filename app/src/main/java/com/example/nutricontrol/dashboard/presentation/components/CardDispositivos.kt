package com.example.nutricontrol.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutricontrol.dashboard.presentation.components.shared.IndicadorCircular

@Composable
fun CardDispositivos(cantidad: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(140.dp)
            .height(230.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                imageVector = Icons.Default.Memory,
                contentDescription = "IoT",
                tint = Color(0xFF024728),
                modifier = Modifier.size(70.dp)
            )

            Text(
                text = "Dispositivos\nIoT:",
                fontSize = 20.sp,
                color = Color(0xFF024728),
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp
            )

            Text(
                text = "$cantidad",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF024728)
            )
        }
    }
}