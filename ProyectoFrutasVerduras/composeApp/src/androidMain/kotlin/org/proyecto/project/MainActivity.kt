package org.proyecto.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()  // ✅ debe ir ANTES de todo
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            BackHandler(enabled = true) {}

            var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {
                SplashScreen(onFinish = { showSplash = false })
            } else {
                App()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}