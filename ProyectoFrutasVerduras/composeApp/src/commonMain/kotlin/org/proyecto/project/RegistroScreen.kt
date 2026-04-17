package org.proyecto.project

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegistroScreen(
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { 
                    ignoreUnknownKeys = true 
                    isLenient = true
                })
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF5CFF5C), Color(0xFF063D06)))
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(Color.White.copy(alpha = 0.28f))
                .border(1.5.dp, Brush.linearGradient(listOf(Color.White.copy(0.9f), Color.White.copy(0.4f))), RoundedCornerShape(36.dp))
                .padding(40.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = InterFont()
                )
                
                Spacer(Modifier.height(8.dp))
                
                Text(
                    text = "Únete a FreshSeason",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = InterFont()
                )

                Spacer(Modifier.height(32.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo", color = Color.White) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(0.5f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                    enabled = !loading
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico", color = Color.White) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(0.5f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                    enabled = !loading
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña", color = Color.White) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(0.5f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                    enabled = !loading
                )

                Spacer(Modifier.height(32.dp))

                ModernButton(
                    text = if (loading) "Registrando..." else "Registrarse",
                    onClick = {
                        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                            errorMessage = "Completa todos los campos"
                        } else {
                            scope.launch {
                                loading = true
                                errorMessage = null
                                try {
                                    val response: String = client.submitForm(
                                        url = "http://192.168.1.7/freshseason_api/register.php",
                                        formParameters = parameters {
                                            append("nombre", nombre)
                                            append("email", email)
                                            append("contrasena", password)
                                        }
                                    ).body()
                                    
                                    // Asumiendo que el PHP devuelve un JSON con success:true
                                    // Por simplicidad si no lanza error, asumimos éxito o validamos el string
                                    onRegisterSuccess()
                                } catch (e: Exception) {
                                    errorMessage = "Error: ${e.message}"
                                } finally {
                                    loading = false
                                }
                            }
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))

                errorMessage?.let {
                    Text(it, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                }

                Text(
                    text = "Volver",
                    color = Color.White.copy(0.7f),
                    modifier = Modifier.clickable { if (!loading) onBackClick() }
                )
            }
        }
    }
}
