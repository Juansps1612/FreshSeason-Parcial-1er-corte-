package org.proyecto.project

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf("presentation") }

    MaterialTheme {
        when (currentScreen) {
            "presentation" -> PresentationScreen(
                onStartClick = {
                    currentScreen = "login" // primero va a login
                }
            )

            "login" -> LoginScreen(
                onLoginClick = { _, _ -> /* ignorado por ahora */ },
                onBackClick = {
                    currentScreen = "presentation"
                },
                onNavigateToHome = {
                    // 👇 este callback se ejecuta DESPUÉS de la animación de salida en LoginScreen
                    currentScreen = "home"
                }
            )

            "home" -> HomeScreen()

            else -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pantalla no implementada",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
