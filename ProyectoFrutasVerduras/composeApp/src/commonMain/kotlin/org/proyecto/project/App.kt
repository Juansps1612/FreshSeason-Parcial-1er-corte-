package org.proyecto.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.proyecto.project.model.LoginResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@Composable
fun App() {

    var currentScreen by remember { mutableStateOf("presentation") }

    // Cliente HTTP configurado correctamente
    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }

    MaterialTheme {
        when (currentScreen) {

            "presentation" -> PresentationScreen(
                onStartClick = {
                    currentScreen = "login"
                }
            )

            "login" -> LoginScreen(
                // Ya no pasamos onLoginClick
                onBackClick = {
                    currentScreen = "presentation"
                },
                onNavigateToHome = {
                    currentScreen = "home"
                }
            )

            "home" -> HomeScreen()

            else -> {
                androidx.compose.foundation.layout.Box(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = "Pantalla no implementada",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}