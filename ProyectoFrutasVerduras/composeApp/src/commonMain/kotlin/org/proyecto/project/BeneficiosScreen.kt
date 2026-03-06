package org.proyecto.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

// -------------------- MODELO DE DATOS --------------------
data class BeneficioCategoria(
    val nombre: String,
    val icono: Int,
    val color: Color,
    val beneficios: List<String>
)

data class TipSalud(
    val titulo: String,
    val descripcion: String,
    val icono: String
)

// -------------------- BENEFICIOS SCREEN --------------------
@Composable
fun BeneficiosScreen() {

    val tipDelDia = remember { tipsSalud[Random.nextInt(tipsSalud.size)] }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Header con título
        Text(
            text = "✨ Beneficios para tu salud",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(24.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Tarjeta de Tip del Día
            item {
                TipDelDiaCard(tip = tipDelDia)
            }

            // Beneficios por categorías
            item {
                Text(
                    text = "🥗 Beneficios por categoría",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(categoriasBeneficios) { categoria ->
                CategoriaBeneficioCard(categoria = categoria)
            }

            // Frase motivacional
            item {
                FraseMotivacionalCard()
            }

            // Espacio al final
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

// -------------------- TARJETA DE TIP DEL DÍA --------------------
@Composable
fun TipDelDiaCard(tip: TipSalud) {
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
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tip.icono,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Tip del día",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = tip.titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = tip.descripcion,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// -------------------- TARJETA DE CATEGORÍA DE BENEFICIO (GLASS) --------------------
@Composable
fun CategoriaBeneficioCard(categoria: BeneficioCategoria) {
    var expandido by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { expandido = !expandido },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))
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
                    RoundedCornerShape(20.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                // Fila superior
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono con fondo glass
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        categoria.color.copy(alpha = 0.3f),
                                        categoria.color.copy(alpha = 0.2f)
                                    )
                                )
                            )
                            .border(
                                1.dp,
                                categoria.color.copy(alpha = 0.4f),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = categoria.icono),
                            contentDescription = null,
                            tint = categoria.color,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Título
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = categoria.nombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${categoria.beneficios.size} beneficios",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    // Indicador expandir
                    Text(
                        text = if (expandido) "▼" else "▶",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Lista de beneficios (expandible)
                if (expandido) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Divisor glass
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.1f),
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                )
                            )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    categoria.beneficios.forEach { beneficio ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "•",
                                fontSize = 16.sp,
                                color = categoria.color,
                                modifier = Modifier.width(16.dp)
                            )
                            Text(
                                text = beneficio,
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------- TARJETA DE FRASE MOTIVACIONAL --------------------
@Composable
fun FraseMotivacionalCard() {
    val frases = listOf(
        "Comer frutas y verduras es el primer paso hacia una vida más saludable 🌱",
        "Tu cuerpo te agradecerá cada bocado de comida natural 💚",
        "Los alimentos de temporada tienen más nutrientes y mejor sabor 🍅",
        "Una manzana al día mantiene al doctor en la lejanía 🍎",
        "Las verduras de hoja verde son pura energía vital 🥬"
    )
    val fraseActual = remember { frases[Random.nextInt(frases.size)] }

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
                Text(
                    text = "💚",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Text(
                    text = fraseActual,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// -------------------- DATOS DE EJEMPLO --------------------
val categoriasBeneficios = listOf(
    BeneficioCategoria(
        "Vitaminas",
        R.drawable.fruta,
        Color(0xFFFFA726),
        listOf(
            "Vitamina A: Mejora la visión y la piel",
            "Vitamina C: Fortalece el sistema inmunológico",
            "Vitamina E: Poderoso antioxidante",
            "Complejo B: Energía y sistema nervioso",
            "Vitamina K: Coagulación y huesos fuertes"
        )
    ),
    BeneficioCategoria(
        "Minerales",
        R.drawable.fruta,
        Color(0xFF66BB6A),
        listOf(
            "Potasio: Regula la presión arterial",
            "Calcio: Huesos y dientes fuertes",
            "Magnesio: Relajación muscular",
            "Hierro: Combate la anemia",
            "Zinc: Fortalece las defensas"
        )
    ),
    BeneficioCategoria(
        "Antioxidantes",
        R.drawable.fruta,
        Color(0xFFEF5350),
        listOf(
            "Licopeno: Protege la próstata",
            "Flavonoides: Antiinflamatorio natural",
            "Betacaroteno: Previene el envejecimiento",
            "Resveratrol: Cardiovascular",
            "Antocianinas: Memoria y cerebro"
        )
    ),
    BeneficioCategoria(
        "Fibra y Digestión",
        R.drawable.fruta,
        Color(0xFF8D6E63),
        listOf(
            "Fibra soluble: Controla el colesterol",
            "Fibra insoluble: Previene estreñimiento",
            "Prebióticos: Alimenta la flora intestinal",
            "Digestión más ligera",
            "Sensación de saciedad"
        )
    )
)

val tipsSalud = listOf(
    TipSalud(
        "🥑 Grasas saludables",
        "El aguacate contiene grasas monoinsaturadas que protegen tu corazón y reducen el colesterol malo.",
        "🥑"
    ),
    TipSalud(
        "🍊 Vitamina C natural",
        "Una naranja al día cubre el 100% de tus necesidades de vitamina C. Mejor entera que en jugo.",
        "🍊"
    ),
    TipSalud(
        "🥦 El rey verde",
        "El brócoli tiene más vitamina C que una naranja y es rico en calcio. Cómelo al vapor para conservar nutrientes.",
        "🥦"
    ),
    TipSalud(
        "🍎 La manzana diaria",
        "Comer una manzana con cáscara aporta pectina, una fibra que ayuda a controlar el colesterol.",
        "🍎"
    ),
    TipSalud(
        "🥕 Visión saludable",
        "La zanahoria es rica en betacaroteno, que tu cuerpo convierte en vitamina A para una vista sana.",
        "🥕"
    )
)