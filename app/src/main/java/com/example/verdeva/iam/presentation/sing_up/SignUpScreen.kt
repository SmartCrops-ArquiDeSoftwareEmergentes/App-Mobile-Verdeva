package com.example.verdeva.iam.presentation.sing_up

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import com.example.verdeva.R

// ---------- Modelos ----------

data class RegisterRequest(
    val username: String,
    val dniOrRuc: String,
    val role: String = "Farmer",
    val emailAddress: String,
    val phone: String,
    val passwordHashed: String,
    val confirmPassword: String
)

data class RegisterResponse(val id: Int)

// ---------- API ----------

interface UserApiService {
    @POST("api/v1/User/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://verdeva-ayagdeb0dceddwgw.canadacentral-01.azurewebsites.net/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val userApi = retrofit.create(UserApiService::class.java)

// ---------- UI ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

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
            Text("Empieza a cultivar resultados", color = Color(0xFF2E5130))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Regístrate", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF024728))

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI o RUC") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Button(
                onClick = {
                    if (username.length !in 2..50) {
                        message = "El nombre debe tener entre 2 y 50 caracteres."
                        return@Button
                    }
                    if (!dni.matches(Regex("^\\d{8,11}$"))) {
                        message = "DNI/RUC debe tener entre 8 y 11 dígitos."
                        return@Button
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        message = "Correo electrónico inválido."
                        return@Button
                    }
                    if (!phone.matches(Regex("^\\d{9,12}$"))) {
                        message = "Teléfono debe tener entre 9 y 12 dígitos."
                        return@Button
                    }
                    if (password.length !in 8..25 || !password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,255}$"))) {
                        message = "La contraseña debe tener mayúscula, minúscula, número y símbolo."
                        return@Button
                    }
                    if (password != confirmPassword) {
                        message = "Las contraseñas no coinciden."
                        return@Button
                    }

                    isLoading = true
                    message = ""

                    CoroutineScope(Dispatchers.IO).launch {
                        val req = RegisterRequest(
                            username = username,
                            dniOrRuc = dni,
                            emailAddress = email,
                            phone = phone,
                            passwordHashed = password,
                            confirmPassword = confirmPassword
                        )

                        try {
                            val response = userApi.register(req)
                            val errorBody = response.errorBody()?.string()
                            Log.d("SignUp", "Response: ${response.code()} - $errorBody")

                            withContext(Dispatchers.Main) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    message = "Error en el registro: ${response.code()}\n$errorBody"
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("SignUp", "Error de red: ", e)
                            withContext(Dispatchers.Main) {
                                isLoading = false
                                message = "Error de red: ${e.localizedMessage}"
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text("Registrarse")
            }

            if (message.isNotEmpty()) {
                val isSuccess = message.contains("exitoso", ignoreCase = true)
                Text(
                    message,
                    color = if (isSuccess) Color(0xFF2E5130) else Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.dp)
                        .background(Color(0xFF024728))
                )
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString("¿Tienes una cuenta? Inicia sesión"),
                    onClick = { navController.popBackStack("sign_in", false) },
                    style = LocalTextStyle.current.copy(fontSize = 14.sp, color = Color(0xFF024728))
                )
            }
        }
    }
}
