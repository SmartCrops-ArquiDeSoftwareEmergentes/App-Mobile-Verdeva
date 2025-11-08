package com.example.nutricontrol.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nutricontrol.analysis.presentation.AnalisisScreen

import com.example.nutricontrol.campos.CamposScreen

import com.example.nutricontrol.dashboard.presentation.DashboardScreen
import com.example.nutricontrol.iam.presentation.sign_in.SignInScreen
import com.example.nutricontrol.iam.presentation.sing_up.SignUpScreen
import com.example.nutricontrol.membresias.MembresiaScreen
import com.example.nutricontrol.profiles.ProfileScreen
import com.example.nutricontrol.recomendacion.RecomendacionScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = "sign_in") {
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                onMiPerfilClick = { navController.navigate("profile") },
                onCerrarSesionClick = { navController.navigate("sign_in") }
            )
        }
        composable("profile") {
            ProfileScreen(
                onMiPerfilClick = { navController.navigate("profile") },
                onLogoutClick = { navController.navigate("sign_in") }
            )
        }
        composable("campos") {
            CamposScreen(
                navController = navController,
                onMiPerfilClick = { navController.navigate("profile") },
                onLogoutClick = { navController.navigate("sign_in") }
            )
        }
        composable("recomendation") {
            RecomendacionScreen(
                navController = navController,
                onMiPerfilClick = { navController.navigate("profile") },
                onLogoutClick = { navController.navigate("sign_in") }
            )
        }
        composable("analysis") {
            AnalisisScreen (
                navController = navController,
                onMiPerfilClick = { navController.navigate("profile") },
                onLogoutClick = { navController.navigate("sign_in") }
            )
        }
        composable("membresia") {
            MembresiaScreen(
                navController = navController,
                onMiPerfilClick = { navController.navigate("profile") },
                onLogoutClick = { navController.navigate("sign_in") }
            )
        }
        composable("sign_in") {
            SignInScreen(navController)
        }
        composable("sign_up") {
            SignUpScreen(navController)
        }
    }
}