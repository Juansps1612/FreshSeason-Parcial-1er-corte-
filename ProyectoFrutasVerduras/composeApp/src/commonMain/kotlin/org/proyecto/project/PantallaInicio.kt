package org.proyecto.project

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {

    // Controla el progreso de carga (0f → 1f)
    var progress by remember { mutableStateOf(0f) }

    // Animación de escala del logo (efecto "latido" suave)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoScale"
    )

    // Animación de opacidad del punto verde del pill
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    // Animación progress animada de forma progresiva
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
        label = "progressBar"
    )

    // Simula la carga y llama a onFinish cuando termina
    LaunchedEffect(Unit) {
        delay(700)
        progress = 0.35f
        delay(900)
        progress = 0.62f
        delay(900)
        progress = 0.82f
        delay(800)
        progress = 1f
        delay(1000)
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xFF042E04),
                        0.45f to Color(0xFF063D06),
                        0.75f to Color(0xFF0A5A0A),
                        1.0f to Color(0xFF5CFF5C)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Círculo decorativo superior izquierdo
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopStart)
                .offset(x = (-70).dp, y = (-50).dp)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            Color(0xFF5CFF5C).copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        // Círculo decorativo inferior derecho
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 40.dp)
                .background(
                    brush = Brush.radialGradient(
                        listOf(
                            Color(0xFF5CFF5C).copy(alpha = 0.09f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // -------- LOGO --------
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .scale(logoScale)
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color.White.copy(alpha = 0.13f))
                    .border(
                        2.dp,
                        Color.White.copy(alpha = 0.28f),
                        RoundedCornerShape(36.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo FreshSeason",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(20.dp))
                )

            }

            Spacer(modifier = Modifier.height(28.dp))

            // -------- NOMBRE --------
            Text(
                text = "FreshSeason",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF7CFF7C),
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // -------- TAGLINE --------
            Text(
                text = "Come de temporada, vive mejor",
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.65f),
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // -------- PILL GLASSMORPHISM --------
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(99.dp))
                    .background(Color.White.copy(alpha = 0.09f))
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.18f),
                        RoundedCornerShape(99.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7CFF7C).copy(alpha = dotAlpha))
                )
                Text(
                    text = "Productos frescos y locales",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.72f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // -------- BARRA DE PROGRESO --------
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .width(180.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(99.dp)),
                    color = Color(0xFF7CFF7C),
                    trackColor = Color.White.copy(alpha = 0.14f),
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "CARGANDO...",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.42f),
                    letterSpacing = 1.8.sp
                )
            }
        }

        // -------- VERSION --------
        Text(
            text = "v1.0.0 · FreshSeason App",
            fontSize = 11.sp,
            color = Color.White.copy(alpha = 0.22f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
        )
    }
}
