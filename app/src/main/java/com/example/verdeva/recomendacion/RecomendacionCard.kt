package com.example.verdeva.recomendacion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verdeva.R

@Composable
fun RecomendacionCard(
    title: String,
    bullets: List<String>,
    modifier: Modifier = Modifier       // <— nuevo parámetro
) {
    Card(
        modifier = modifier,             // <— lo uso aquí
        elevation = CardDefaults.cardElevation(4.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        shape     = CardDefaults.shape
    ) {
        Column(Modifier.padding(16.dp)) {
            Image(
                painter           = painterResource(id = R.drawable.ic_field_icon),
                contentDescription = null,
                modifier           = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                title,
                fontSize   = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color      = Color(0xFF024728)
            )
            Spacer(Modifier.height(8.dp))
            bullets.forEach { punto ->
                Text(
                    "• $punto",
                    fontSize = 14.sp,
                    color    = Color.Black,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }
    }
}