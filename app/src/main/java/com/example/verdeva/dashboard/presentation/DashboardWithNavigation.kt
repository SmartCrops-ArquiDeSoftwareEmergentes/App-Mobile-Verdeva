package com.example.verdeva.dashboard.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.verdeva.dashboard.presentation.components.BottomNavigationBar
import androidx.navigation.compose.rememberNavController
import com.example.verdeva.navigation.NavGraph


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardWithNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        // Asegúrate de pasar correctamente el navController
        NavGraph(navController = navController, paddingValues = paddingValues)
    }
}