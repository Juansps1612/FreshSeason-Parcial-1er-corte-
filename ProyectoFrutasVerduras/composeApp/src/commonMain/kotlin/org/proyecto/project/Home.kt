package org.proyecto.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

// -------------------- ICONOS --------------------
sealed class AppIcon {
    object Home : AppIcon()
    object Calendar : AppIcon()
    object Search : AppIcon()
    object Favorite : AppIcon()
    object Benefits : AppIcon()
}

// -------------------- ITEM MENU --------------------
data class BottomNavItem(
    val label: String,
    val icon: AppIcon
)

// -------------------- CONSEJOS --------------------
val consejos = listOf(
    "Consumir alimentos de temporada reduce la huella de carbono.",
    "Comprar productos locales ayuda a los agricultores.",
    "Las frutas de temporada tienen mejor sabor.",
    "Los alimentos frescos conservan más nutrientes.",
    "Evita el desperdicio comprando solo lo necesario.",
    "Las verduras de temporada suelen ser más económicas.",
    "Consumir local apoya la economía regional.",
    "Los alimentos frescos requieren menos transporte.",
    "Cultivar en casa reduce el impacto ambiental.",
    "Elegir productos naturales mejora tu salud."
)

// -------------------- HOME --------------------
// -------------------- HOME --------------------
// -------------------- HOME --------------------
@Composable
fun HomeScreen() {

    var selectedItem by remember { mutableStateOf(0) }
    var mostrarPerfil by remember { mutableStateOf(false) } // Nuevo estado para perfil
    var recetaDesplegada by remember { mutableStateOf(false) }

    // Cambiar consejo cuando se selecciona Home (índice 0)
    val consejoActual = remember(selectedItem) {
        if (selectedItem == 0) {
            consejos[Random.nextInt(consejos.size)]
        } else {
            consejos[0]
        }
    }

    val items = listOf(
        BottomNavItem("Inicio", AppIcon.Home),
        BottomNavItem("Temporada", AppIcon.Calendar),
        BottomNavItem("Buscar", AppIcon.Search),
        BottomNavItem("Favoritos", AppIcon.Favorite),
        BottomNavItem("Beneficios", AppIcon.Benefits)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF5CFF5C),
                        Color(0xFF063D06)
                    )
                )
            )
    ) {

        // ---------------- HEADER ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF042E04),
                            Color(0xFF021A02)
                        )
                    )
                )
                .padding(top = 50.dp, bottom = 14.dp, start = 24.dp, end = 24.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "FreshSeason 🌱",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7CFF7C)
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            if (mostrarPerfil) {
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF6EC6FF).copy(alpha = 0.3f),
                                        Color(0xFF4A9EFF).copy(alpha = 0.2f)
                                    )
                                )
                            } else {
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.28f),
                                        Color.White.copy(alpha = 0.18f)
                                    )
                                )
                            }
                        )
                        .border(
                            1.5.dp,
                            if (mostrarPerfil) {
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF6EC6FF).copy(alpha = 0.9f),
                                        Color(0xFF4A9EFF).copy(alpha = 0.4f)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.9f),
                                        Color.White.copy(alpha = 0.4f)
                                    )
                                )
                            },
                            RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            mostrarPerfil = !mostrarPerfil
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.editar_usuario),
                        contentDescription = "Perfil",
                        modifier = Modifier.size(26.dp),
                        colorFilter = ColorFilter.tint(
                            if (mostrarPerfil)
                                Color(0xFF6EC6FF)
                            else
                                Color.White
                        )
                    )
                }
            }
        }

        // ---------------- CONTENIDO VARIABLE SEGÚN PESTAÑA ----------------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, bottom = 100.dp)
        ) {
            if (mostrarPerfil) {
                // Mostrar pantalla de perfil
                PerfilScreen()
            } else {
                when (selectedItem) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                GlassCard(
                                    title = "🌍 Consejo sostenible",
                                    text = consejoActual,
                                    isClickable = false
                                )
                            }
                            item {
                                GlassCard(
                                    title = "🍎 Fruta del día",
                                    text = "La manzana es rica en fibra y antioxidantes.",
                                    isClickable = false
                                )
                            }
                            item {
                                GlassCard(
                                    title = "🥦 Verdura recomendada",
                                    text = "El brócoli fortalece el sistema inmunológico.",
                                    isClickable = false
                                )
                            }
                            item {
                                GlassCard(
                                    title = "💪 Beneficio nutricional",
                                    text = "Las frutas cítricas aportan vitamina C.",
                                    isClickable = false
                                )
                            }
                            item {
                                GlassCard(
                                    title = "🧊 Tip de almacenamiento",
                                    text = "Guarda las verduras de hoja en recipientes herméticos.",
                                    isClickable = false
                                )
                            }
                            item {
                                GlassCard(
                                    title = "🍽 Receta del día",
                                    text = "Ensalada de temporada",
                                    isClickable = true,
                                    onClick = { recetaDesplegada = !recetaDesplegada }
                                )
                                if (recetaDesplegada) {
                                    RecetaDetalleCard(
                                        nombreReceta = "Ensalada de temporada",
                                        preparacion = "1. Lava bien los vegetales\n" +
                                                "2. Corta el tomate y aguacate en cubos\n" +
                                                "3. Mezcla con hojas verdes\n" +
                                                "4. Aliña con jugo de limón, aceite y sal\n" +
                                                "5. ¡Disfruta tu ensalada fresca!"
                                    )
                                }
                            }
                        }
                    }
                    1 -> TemporadaScreen()
                    2 -> BuscarScreen()
                    3 -> FavoritosScreen()
                    4 -> BeneficiosScreen()
                }
            }
        }

        // ---------------- BOTTOM BAR FLOTANTE ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingBottomBarLoginStyle(
                selectedItem = if (mostrarPerfil) -1 else selectedItem, // Si está en perfil, ningún item seleccionado
                onItemSelected = {
                    selectedItem = it
                    mostrarPerfil = false // Cerrar perfil al cambiar de pestaña
                },
                items = items
            )
        }
        }
    }

// -------------------- TARJETA DE RECETA DETALLADA (mismo diseño) --------------------
// -------------------- TARJETA DE RECETA DETALLADA --------------------
@Composable
fun RecetaDetalleCard(
    nombreReceta: String,
    preparacion: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Se ajusta al contenido
            .padding(horizontal = 24.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
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
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "🍽 $nombreReceta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = preparacion,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    textAlign = TextAlign.Start,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
// -------------------- TARJETA MODIFICADA --------------------
@Composable
fun GlassCard(
    title: String,
    text: String,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Se ajusta al contenido
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .then(
                if (isClickable) {
                    Modifier.clickable { onClick?.invoke() }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
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
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp), // Padding original
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    // Eliminamos maxLines y overflow para que se muestre completo
                )
            }
        }
    }
}

// -------------------- TARJETA DE RECETA DETALLADA --------------------
@Composable
fun RecetaDetalleCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Más altura para la receta detallada
            .padding(horizontal = 24.dp, vertical = 10.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.4f),
                            Color(0xFF2E7D32).copy(alpha = 0.3f)
                        )
                    )
                )
                .border(
                    2.dp,
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFA5D6A5),
                            Color(0xFF81C784)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "🥗 Cómo prepararla",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "1. Lava bien los vegetales\n" +
                            "2. Corta el tomate y aguacate en cubos\n" +
                            "3. Mezcla con hojas verdes\n" +
                            "4. Aliña con jugo de limón, aceite y sal\n" +
                            "5. ¡Disfruta tu ensalada fresca!",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    textAlign = TextAlign.Start,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
// -------------------- TARJETA --------------------
@Composable
fun GlassCard(title: String, text: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .clickable { },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Box(
            modifier = Modifier
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
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -------------------- BOTTOM BAR --------------------
@Composable
fun FloatingBottomBarLoginStyle(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
    items: List<BottomNavItem>
) {

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(80.dp)
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(36.dp)
            )
            .clip(RoundedCornerShape(36.dp))
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
                RoundedCornerShape(36.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEachIndexed { index, item ->

                val isSelected = index == selectedItem

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .clickable { onItemSelected(index) }
                ) {

                    PlatformIcon(item.icon, isSelected)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected)
                            Color(0xFF6EC6FF)
                        else
                            Color.White.copy(alpha = 0.75f)
                    )
                }
            }
        }
    }
}
// -------------------- ICONOS --------------------
@Composable
fun PlatformIcon(icon: AppIcon, isSelected: Boolean) {

    val iconResource = when (icon) {
        AppIcon.Home -> R.drawable.casa
        AppIcon.Calendar -> R.drawable.calendario
        AppIcon.Search -> R.drawable.lupa
        AppIcon.Favorite -> R.drawable.favorito
        AppIcon.Benefits -> R.drawable.fruta
    }

    Image(
        painter = painterResource(id = iconResource),
        contentDescription = null,
        modifier = Modifier.size(26.dp),
        colorFilter = ColorFilter.tint(
            if (isSelected)
                Color(0xFF6EC6FF)
            else
                Color.White.copy(alpha = 0.7f)
        )
    )
}