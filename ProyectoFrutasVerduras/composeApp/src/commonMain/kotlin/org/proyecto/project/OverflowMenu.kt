package org.proyecto.project

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties

@Composable
fun OverflowMenu() {

    var expanded by remember { mutableStateOf(false) }
    var dialogItem by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current

    // -------- Botón de tres puntos --------
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (expanded)
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF6EC6FF).copy(alpha = 0.3f),
                            Color(0xFF4A9EFF).copy(alpha = 0.2f)
                        )
                    )
                else
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.18f)
                        )
                    )
            )
            .border(
                1.5.dp,
                if (expanded)
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF6EC6FF).copy(alpha = 0.9f),
                            Color(0xFF4A9EFF).copy(alpha = 0.4f)
                        )
                    )
                else
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                RoundedCornerShape(18.dp)
            )
            .clickable { expanded = true },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "⋮",
            fontSize = 22.sp,
            color = if (expanded) Color(0xFF6EC6FF) else Color.White,
            fontWeight = FontWeight.Bold
        )
    }

    // -------- Menú desplegable --------
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = Color(0xFF0D2E0D),
            surfaceContainer = Color(0xFF0D2E0D)
        )
    ) {

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = (-100).dp, y = 4.dp),
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .border(
                    1.5.dp,
                    Color(0xFF7CFF7C).copy(alpha = 0.25f),
                    RoundedCornerShape(18.dp)
                )
                .width(240.dp),
            properties = PopupProperties(focusable = true)
        ) {

            // Ítem 1 — Equipo
            OverflowMenuItem(
                icon = "👥",
                label = "Equipo del proyecto",
                sublabel = "Integrantes · FreshSeason App",
                onClick = {
                    expanded = false
                    dialogItem = 1
                }
            )

            HorizontalDivider(
                color = Color.White.copy(alpha = 0.08f),
                thickness = 1.dp
            )

            // Ítem 2 — Acerca de
            OverflowMenuItem(
                icon = "📋",
                label = "Acerca de la app",
                sublabel = "Descripción · Asignatura · TFC",
                onClick = {
                    expanded = false
                    dialogItem = 2
                }
            )

            HorizontalDivider(
                color = Color.White.copy(alpha = 0.08f),
                thickness = 1.dp
            )

            // Ítem 3 — Salir
            OverflowMenuItem(
                icon = "🚪",
                label = "Salir de la app",
                sublabel = "Cerrar la aplicación",
                labelColor = Color(0xFFFF7C7C),
                onClick = {
                    expanded = false
                    (context as? Activity)?.finishAffinity()
                }
            )
        }
    }

    // -------- Diálogos --------
    when (dialogItem) {

        1 -> OverflowDialog(
            onDismiss = { dialogItem = null }
        ) {

            DialogContent(
                title = "👥 Equipo del proyecto",
                body = """
                    App: FreshSeason 🌱

                    Integrantes:
                    • José Luis Martínez Acevedo
                    • Juan Sebastián Pinzón Sánchez
                """.trimIndent()
            )
        }

        2 -> OverflowDialog(
            onDismiss = { dialogItem = null }
        ) {

            DialogContent(
                title = "📋 Acerca de la app",
                body = """
                    FreshSeason es una app móvil enfocada en promover el consumo saludable mediante la visualización de frutas y verduras de temporada.

                    Asignatura: Desarrollo de Apps Móviles
                    Trabajo Final de Curso — TFC
                    Periodo académico: 2026
                """.trimIndent()
            )
        }
    }
}

// -------- Subcomposables de apoyo --------

@Composable
fun OverflowMenuItem(
    icon: String,
    label: String,
    sublabel: String,
    labelColor: Color = Color(0xFF7CFF7C),
    onClick: () -> Unit
) {

    DropdownMenuItem(

        text = {

            Column(
                modifier = Modifier.padding(vertical = 2.dp)
            ) {

                Text(
                    text = "$icon  $label",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = labelColor
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = sublabel,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        },

        onClick = onClick,

        colors = MenuDefaults.itemColors(
            textColor = Color.White
        )
    )
}

@Composable
fun OverflowDialog(
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        containerColor = Color(0xFF0D2E0D),

        shape = RoundedCornerShape(24.dp),

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Cerrar",
                    color = Color(0xFF7CFF7C),
                    fontWeight = FontWeight.Bold
                )
            }
        },

        text = {
            Column(content = content)
        }
    )
}

@Composable
fun DialogContent(
    title: String,
    body: String
) {

    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF7CFF7C)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = body,
        fontSize = 13.sp,
        color = Color.White.copy(alpha = 0.85f),
        lineHeight = 20.sp
    )
}