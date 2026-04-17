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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.proyecto.project.model.LoginResponse

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    favoritosViewModel: FavoritosViewModel
) {
    var visible by remember { mutableStateOf(false) }
    var exiting by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Necesitamos el ViewModel de favoritos para cargar los datos al entrar

    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                }, contentType = ContentType.Any)
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    val screenAlpha by animateFloatAsState(
        targetValue = if (exiting) 0f else 1f,
        animationSpec = tween(1200),
        label = ""
    )

    val screenScale by animateFloatAsState(
        targetValue = if (exiting) 0.95f else 1f,
        animationSpec = tween(1200),
        label = ""
    )

    LaunchedEffect(exiting) {
        if (exiting) {
            delay(1300)
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = screenAlpha
                scaleX = screenScale
                scaleY = screenScale
            }
            .background(
                Brush.verticalGradient(listOf(Color(0xFF5CFF5C), Color(0xFF063D06)))
            ),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(700)) + slideInVertically(initialOffsetY = { it / 2 },
                animationSpec = tween(700)
            )
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

                    Text("FreshSeason", fontSize = 38.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = InterFont())
                    Spacer(Modifier.height(12.dp))
                    Text("Inicia sesión para continuar", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(0.95f), fontFamily = InterFont())

                    Spacer(Modifier.height(36.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico", color = Color.White) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White.copy(0.9f),
                            unfocusedBorderColor = Color.White.copy(0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                        enabled = !loading
                    )

                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", color = Color.White) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White.copy(0.9f),
                            unfocusedBorderColor = Color.White.copy(0.5f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                        enabled = !loading
                    )

                    Spacer(Modifier.height(40.dp))

                    ModernButton(
                        text = if (loading) "Iniciando..." else "Iniciar Sesión",
                        onClick = {
                            if (!loading) {
                                scope.launch {
                                    if (email.isBlank() || password.isBlank()) {
                                        errorMessage = "Completa todos los campos"
                                        return@launch
                                    }

                                    loading = true
                                    errorMessage = null

                                    try {
                                        // Usamos la IP del emulador directamente
                                        val response: LoginResponse = client.get(
                                            "http://192.168.1.7/freshseason_api/login.php?email=$email&contrasena=$password"
                                        ).body()

                                        if (response.success) {
                                            val user = response.user
                                            if (user == null) {
                                                errorMessage = "Respuesta de login sin datos de usuario"
                                                return@launch
                                            }
                                            val uid = user.id ?: 0
                                            if (uid == 0) {
                                                errorMessage =
                                                    "El servidor no envió un id de usuario válido (revisa login.php / JSON)."
                                                return@launch
                                            }
                                            favoritosViewModel.setUser(uid)
                                            SessionManager.nombre = user.nombre ?: ""
                                            SessionManager.email = user.email ?: ""
                                            SessionManager.membresia = user.membresia ?: "Free"
                                            SessionManager.fechaRegistro = user.fechaRegistro.toString()
                                            exiting = true
                                        } else {
                                            errorMessage = response.message ?: "Correo o contraseña incorrectos"
                                        }

                                    } catch (e: Exception) {
                                        errorMessage = "Error de conexión: ${e.message}"
                                    } finally {
                                        loading = false
                                    }
                                }
                            }
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    errorMessage?.let {
                        Text(it, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.Center) {
                        Text("¿No tienes cuenta? ", color = Color.White.copy(0.85f))
                        Text("Regístrate", color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { })
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Volver",
                        color = Color.White.copy(0.7f),
                        modifier = Modifier.clickable { if (!loading) onBackClick() }
                    )
                }
            }
        }
    }
}