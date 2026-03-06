package org.proyecto.project

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PresentationScreen(
    onStartClick: () -> Unit
) {

    var visible by remember { mutableStateOf(false) }
    var exiting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    // 🔥 Animación global de salida
    val screenAlpha by animateFloatAsState(
        targetValue = if (exiting) 0f else 1f,
        animationSpec = tween(1200), // más lenta
        label = ""
    )

    val screenScale by animateFloatAsState(
        targetValue = if (exiting) 0.95f else 1f,
        animationSpec = tween(1200),
        label = ""
    )

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
            enter = fadeIn(tween(700)) +
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
                        text = "Descubre productos frescos según la temporada",
                        fontSize = 18.sp, // ⬆ más grande
                        fontWeight = FontWeight.Medium, // ⬆ más rellena
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = InterFont()
                    )


                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "Proyecto académico",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium, // más gruesa
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = InterFont()
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "José Luis Martínez\nJuan Sebastián Pinzón",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium, // más relleno
                        lineHeight = 22.sp, // mejora lectura
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = InterFont()
                    )


                    Spacer(modifier = Modifier.height(40.dp))

                    ModernButton(
                        text = "Iniciar",
                        onClick = {
                            exiting = true
                        }
                    )

                    if (exiting) {
                        LaunchedEffect(Unit) {
                            delay(1300)
                            onStartClick()
                        }
                    }
                }
                }
            }
        }
    }


@Composable
fun ModernButton(
    text: String,
    onClick: () -> Unit
) {

    val scope = rememberCoroutineScope()
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(120),
        label = ""
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(50))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.9f),
                        Color.White.copy(alpha = 0.75f)
                    )
                )
            )
            .clickable {
                pressed = true
                scope.launch {
                    delay(120)
                    pressed = false
                }
                onClick()
            }
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF063D06)
        )
    }
}