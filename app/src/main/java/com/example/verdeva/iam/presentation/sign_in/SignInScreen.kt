package com.example.verdeva.iam.presentation.sign_in

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import androidx.datastore.preferences.core.*
import com.example.verdeva.dataStore

/* ------------------ MODELOS ------------------ */

data class SignInRequest(val username: String, val password: String)
data class LoginResponse(val jwt: String)

/* ------------------ API ------------------ */

interface UserApiServiceLogin {
    @POST("api/v1/User/login")
    suspend fun login(@Body request: SignInRequest): Response<LoginResponse>
}

val retrofitLogin = Retrofit.Builder()
    .baseUrl("https://nutricontrolapifilesadministration-bvf4bbbpgpb5h5dw.brazilsouth-01.azurewebsites.net/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val userApiLogin = retrofitLogin.create(UserApiServiceLogin::class.java)

/* ------------------ TOKEN MANAGER ------------------ */

class TokenManager(private val context: Context) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USERNAME_KEY = stringPreferencesKey("username")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
        println("🟢 Token guardado: $token")
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
        println("🟢 Username guardado: $username")
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[TOKEN_KEY] }.first()
    }

    suspend fun getUsername(): String? {
        return context.dataStore.data.map { it[USERNAME_KEY] }.first()
    }

    suspend fun clearAll() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USERNAME_KEY)
        }
    }
}

/* ------------------ UI ------------------ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.height(350.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                drawPath(
                    path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(0f, height * 0.9f)
                        quadraticBezierTo(width * 0.5f, height * 1.00f, width, height * 0.65f)
                        lineTo(width, 0f)
                        close()
                    },
                    color = Color(0xFF024728)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo_nutricontrol),
                contentDescription = "Logo",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Empieza a cultivar resultados", color = Color(0xFF024728))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Inicia sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF024728))

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Nombre de usuario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFF024728),
                    focusedBorderColor = Color(0xFF024728)
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFF024728),
                    focusedBorderColor = Color(0xFF024728)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val req = SignInRequest(username = username, password = password)
                        try {
                            val response = userApiLogin.login(req)
                            if (response.isSuccessful) {
                                val token = response.body()?.jwt ?: ""
                                tokenManager.saveToken(token)
                                tokenManager.saveUsername(username)

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                    navController.navigate("dashboard") {
                                        popUpTo("sign_in") { inclusive = true }
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    message = "Credenciales incorrectas o error: ${response.code()}"
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                message = "Error de red: ${e.localizedMessage}"
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF024728)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Iniciar sesión", color = Color.White, fontSize = 18.sp)
            }

            if (message.isNotEmpty()) {
                Text(message, color = Color.Red, modifier = Modifier.padding(16.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color(0xFF024728))
                )
                Spacer(modifier = Modifier.width(8.dp))
                ClickableText(
                    text = AnnotatedString("¿No tienes una cuenta? Regístrate"),
                    onClick = { navController.navigate("sign_up") },
                    style = LocalTextStyle.current.copy(
                        fontSize = 14.sp,
                        color = Color(0xFF024728)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color(0xFF024728))
                )
            }
        }
    }
}
