package org.proyecto.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

// -------------------- MODELO DE DATOS --------------------
data class EstadisticaPerfil(
    val titulo: String,
    val valor: String,
    val icono: Int,
    val color: Color
)

data class Logro(
    val nombre: String,
    val descripcion: String,
    val icono: String,
    val desbloqueado: Boolean,
    val progreso: Int = 100
)

data class OpcionConfiguracion(
    val titulo: String,
    val icono: Int,
    val ruta: String? = null,
    val esSwitch: Boolean = false,
    var valorSwitch: Boolean = false
)

// -------------------- PERFIL SCREEN --------------------
@Composable
fun PerfilScreen() {

    var notificacionesActivadas by remember { mutableStateOf(true) }
    var modoOscuro by remember { mutableStateOf(false) }
    var opcionesConfig = remember {
        listOf(
            OpcionConfiguracion("Editar perfil", R.drawable.editar_usuario, "editar"),
            OpcionConfiguracion("Notificaciones", R.drawable.campana, esSwitch = true, valorSwitch = true),
            OpcionConfiguracion("Idioma", R.drawable.idioma, "idioma"),
            OpcionConfiguracion("Tema oscuro", R.drawable.luna, esSwitch = true, valorSwitch = false),
            OpcionConfiguracion("Ayuda", R.drawable.ayuda, "ayuda"),
            OpcionConfiguracion("Cerrar sesión", R.drawable.salir, "logout")
        ).toMutableStateList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Tarjeta de perfil principal
            item {
                PerfilPrincipalCard()
            }

            // Estadísticas rápidas
            item {
                Text(
                    text = "📊 Mis estadísticas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                EstadisticasPerfilGrid()
            }

            // Logros recientes
            item {
                Text(
                    text = "🏆 Logros obtenidos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(logrosEjemplo) { logro ->
                LogroCard(logro = logro)
            }

            // Configuración
            item {
                Text(
                    text = "⚙️ Configuración",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(opcionesConfig.size) { index ->
                ConfiguracionItem(
                    opcion = opcionesConfig[index],
                    onSwitchChanged = { nuevoValor ->
                        opcionesConfig[index] = opcionesConfig[index].copy(valorSwitch = nuevoValor)
                    }
                )
            }

            // Espacio final
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// -------------------- TARJETA DE PERFIL PRINCIPAL --------------------
@Composable
fun PerfilPrincipalCard() {
    // Generar iniciales aleatorias para el ejemplo
    val iniciales = remember { "US" }
    val colorAvatar = remember { Color(0xFF4CAF50) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.18f)
                        )
                    )
                )
                .border(
                    2.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar con iniciales
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    colorAvatar,
                                    colorAvatar.copy(alpha = 0.7f)
                                )
                            )
                        )
                        .border(
                            2.dp,
                            Color.White.copy(alpha = 0.5f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciales,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información del usuario
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Usuario FreshSeason",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "usuario@email.com",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Membresía
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.3f))
                            .border(
                                1.dp,
                                Color(0xFF4CAF50).copy(alpha = 0.5f),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Membresía Premium",
                            fontSize = 10.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Botón editar (icono pequeño)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { }
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.3f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.editar_usuario),
                        contentDescription = "Editar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

// -------------------- GRID DE ESTADÍSTICAS DE PERFIL --------------------
@Composable
fun EstadisticasPerfilGrid() {
    val estadisticas = listOf(
        EstadisticaPerfil("Días activo", "127", R.drawable.calendario, Color(0xFFFFA726)),
        EstadisticaPerfil("Favoritos", "24", R.drawable.favorito, Color(0xFFEF5350)),
        EstadisticaPerfil("Búsquedas", "89", R.drawable.lupa, Color(0xFF66BB6A)),
        EstadisticaPerfil("Recetas", "12", R.drawable.receta, Color(0xFF42A5F5))
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Primera fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                EstadisticaPerfilCard(estadistica = estadisticas[0])
            }
            Box(modifier = Modifier.weight(1f)) {
                EstadisticaPerfilCard(estadistica = estadisticas[1])
            }
        }

        // Segunda fila
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                EstadisticaPerfilCard(estadistica = estadisticas[2])
            }
            Box(modifier = Modifier.weight(1f)) {
                EstadisticaPerfilCard(estadistica = estadisticas[3])
            }
        }
    }
}

// -------------------- TARJETA DE ESTADÍSTICA DE PERFIL --------------------
@Composable
fun EstadisticaPerfilCard(estadistica: EstadisticaPerfil) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.18f)
                        )
                    )
                )
                .border(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(estadistica.color.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            estadistica.color.copy(alpha = 0.3f),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = estadistica.icono),
                        contentDescription = null,
                        tint = estadistica.color,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Texto
                Column {
                    Text(
                        text = estadistica.valor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = estadistica.titulo,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

// -------------------- TARJETA DE LOGRO --------------------
@Composable
fun LogroCard(logro: Logro) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.18f)
                        )
                    )
                )
                .border(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono del logro
                Text(
                    text = logro.icono,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )

                // Información
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = logro.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = logro.descripcion,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Indicador de desbloqueado
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (logro.desbloqueado)
                                Color(0xFF4CAF50).copy(alpha = 0.3f)
                            else
                                Color.White.copy(alpha = 0.1f)
                        )
                        .border(
                            1.dp,
                            if (logro.desbloqueado)
                                Color(0xFF4CAF50)
                            else
                                Color.White.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (logro.desbloqueado) {
                        Text(
                            text = "✓",
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// -------------------- ITEM DE CONFIGURACIÓN --------------------
@Composable
fun ConfiguracionItem(
    opcion: OpcionConfiguracion,
    onSwitchChanged: (Boolean) -> Unit
) {
    var switchState by remember { mutableStateOf(opcion.valorSwitch) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(enabled = !opcion.esSwitch) { },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.28f),
                            Color.White.copy(alpha = 0.18f)
                        )
                    )
                )
                .border(
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.3f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = opcion.icono),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Título
                Text(
                    text = opcion.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                // Switch o flecha
                if (opcion.esSwitch) {
                    Switch(
                        checked = switchState,
                        onCheckedChange = {
                            switchState = it
                            onSwitchChanged(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                } else {
                    if (opcion.titulo != "Cerrar sesión") {
                        Text(
                            text = "→",
                            fontSize = 18.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

// -------------------- DATOS DE EJEMPLO --------------------
val logrosEjemplo = listOf(
    Logro(
        "Explorador estacional",
        "Probaste frutas de todas las temporadas",
        "🌍",
        true
    ),
    Logro(
        "Amante de las verduras",
        "Añadiste 10 verduras a favoritos",
        "🥦",
        true
    ),
    Logro(
        "Saludable",
        "Completaste 30 días de racha",
        "💚",
        false,
        60
    ),
    Logro(
        "Master en búsquedas",
        "Realizaste 100 búsquedas",
        "🔍",
        false,
        45
    )
)