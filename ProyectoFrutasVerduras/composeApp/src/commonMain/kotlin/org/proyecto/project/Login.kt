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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    var exiting by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Entrada automática al aparecer la pantalla
    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    // Animación de salida (exactamente igual a PresentationScreen)
    val screenAlpha by animateFloatAsState(
        targetValue = if (exiting) 0f else 1f,
        animationSpec = tween(1200),  // duración igual a la presentación
        label = ""
    )

    val screenScale by animateFloatAsState(
        targetValue = if (exiting) 0.95f else 1f,  // misma reducción de escala
        animationSpec = tween(1200),
        label = ""
    )

    // Cuando se activa exiting → esperar que termine la animación y navegar
    LaunchedEffect(exiting) {
        if (exiting) {
            delay(1300)  // un poco más que la animación para que se vea completa
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
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF5CFF5C),
                        Color(0xFF063D06)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(700)) +
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(700)
                    )
        ) {

            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(
                        Color.White.copy(alpha = 0.28f)
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                Color.White.copy(alpha = 0.9f),
                                Color.White.copy(alpha = 0.4f)
                            )
                        ),
                        shape = RoundedCornerShape(36.dp)
                    )
                    .padding(40.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "FreshSeason",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color.White,
                        fontFamily = InterFont()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Inicia sesión para continuar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = InterFont()
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    // Campo Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White.copy(alpha = 0.9f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White.copy(alpha = 0.9f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { /* TODO */ }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    ModernButton(
                        text = "Iniciar Sesión",
                        onClick = {
                            // Activar la animación de salida (igual que en PresentationScreen)
                            exiting = true
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¿No tienes cuenta? ",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Text(
                            text = "Regístrate",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.clickable { /* TODO */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Volver",
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.clickable {
                            exiting = true
                        }
                    )

                    if (exiting) {
                        // El LaunchedEffect de arriba ya maneja la navegación
                        // Este if no es necesario para navegación, pero lo dejamos por si quieres agregar algo más
                    }
                }
            }
        }
    }
}