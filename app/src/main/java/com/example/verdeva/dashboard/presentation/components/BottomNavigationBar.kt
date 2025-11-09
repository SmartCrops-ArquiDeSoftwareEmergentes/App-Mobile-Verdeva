package com.example.verdeva.dashboard.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(containerColor = Color(0xFF024728)) {
        val selectedColor = Color.White
        val indicatorColor = Color.White.copy(alpha = 0.2f)

        NavigationBarItem(
            selected    = currentRoute == "membresia",
            onClick     = { navController.navigate("membresia") },
            icon = { Icon(Icons.Default.Stars, contentDescription = "Membresía") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                unselectedIconColor = selectedColor,
                indicatorColor = indicatorColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == "analysis",
            onClick = { navController.navigate("analysis") },
            icon = { Icon(Icons.Default.SavedSearch, contentDescription = "Search") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                unselectedIconColor = selectedColor,
                indicatorColor = indicatorColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = { navController.navigate("dashboard") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                unselectedIconColor = selectedColor,
                indicatorColor = indicatorColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == "campos",
            onClick = { navController.navigate("campos") }, // Navegar a CamposScreen
            icon = { Icon(Icons.Default.Warehouse, contentDescription = "Campos") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                unselectedIconColor = selectedColor,
                indicatorColor = indicatorColor
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("recomendation") },
            icon = { Icon(Icons.Default.Lightbulb, contentDescription = "Luz") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                unselectedIconColor = selectedColor,
                indicatorColor = indicatorColor
            )
        )
    }
}