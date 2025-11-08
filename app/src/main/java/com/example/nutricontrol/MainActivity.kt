package com.example.nutricontrol

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.nutricontrol.dashboard.presentation.DashboardWithNavigation
import com.example.nutricontrol.ui.theme.NutriControlTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.nutricontrol.navigation.NavGraph
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavGraph(navController, PaddingValues(0.dp))
        }
    }
}