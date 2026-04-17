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
import androidx.compose.material3.Divider
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
import java.util.Calendar

// -------------------- TEMPORADA SCREEN --------------------
@Composable
fun TemporadaScreen(productos: List<Producto> = todosLosProductos) {

    var mesSeleccionado by remember { mutableStateOf(obtenerMesActual()) }
    var filtroTipo by remember { mutableStateOf("Todos") } // "Todos", "Frutas", "Verduras"

    val productosFiltrados = remember(mesSeleccionado, filtroTipo) {
        productos.filter { producto ->
            // Manejar productos que cruzan el año (como naranja: nov a mar)
            val productoEnRango = if (producto.mesInicio > producto.mesFin) {
                // Caso especial: cruza fin de año
                (mesSeleccionado >= producto.mesInicio || mesSeleccionado <= producto.mesFin)
            } else {
                (mesSeleccionado >= producto.mesInicio && mesSeleccionado <= producto.mesFin)
            }

            productoEnRango && when (filtroTipo) {
                "Frutas" -> producto.tipo == "Fruta"
                "Verduras" -> producto.tipo == "Verdura"
                else -> true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // ---------------- SELECTOR DE MES ----------------
        MesSelector(
            mesSeleccionado = mesSeleccionado,
            onMesSelected = { mesSeleccionado = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- FILTROS ----------------
        FiltrosTipo(
            filtroActual = filtroTipo,
            onFiltroChanged = { filtroTipo = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- TÍTULO DEL MES ----------------
        Text(
            text = "🌱 Productos de ${nombreMes(mesSeleccionado)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ---------------- LISTA DE PRODUCTOS ----------------
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (productosFiltrados.isEmpty()) {
                item {
                    TarjetaSinProductos()
                }
            } else {
                items(productosFiltrados.size) { index ->
                    TarjetaProductoTemporada(
                        producto = productosFiltrados[index]
                    )
                }
            }
        }
    }
}

// -------------------- SELECTOR DE MES --------------------
@Composable
fun MesSelector(
    mesSeleccionado: Int,
    onMesSelected: (Int) -> Unit
) {
    val meses = listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(meses.size) { index ->
                val mes = index + 1
                val isSelected = mes == mesSeleccionado

                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            if (isSelected) {
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF4CAF50),
                                        Color(0xFF2E7D32)
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
                        .then(
                            if (!isSelected) {
                                Modifier.border(
                                    1.5.dp,
                                    Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.9f),
                                            Color.White.copy(alpha = 0.4f)
                                        )
                                    ),
                                    RoundedCornerShape(24.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                        .clickable { onMesSelected(mes) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = meses[index],
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

// -------------------- FILTROS DE TIPO --------------------
@Composable
fun FiltrosTipo(
    filtroActual: String,
    onFiltroChanged: (String) -> Unit
) {
    val filtros = listOf("Todos", "Frutas", "Verduras")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        filtros.forEach { filtro ->
            val isSelected = filtro == filtroActual

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) {
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF4CAF50),
                                    Color(0xFF2E7D32)
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
                    .then(
                        if (!isSelected) {
                            Modifier.border(
                                1.5.dp,
                                Brush.linearGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.9f),
                                        Color.White.copy(alpha = 0.4f)
                                    )
                                ),
                                RoundedCornerShape(20.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable { onFiltroChanged(filtro) }
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filtro,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

// -------------------- TARJETA DE PRODUCTO DE TEMPORADA --------------------
@Composable
fun TarjetaProductoTemporada(
    producto: Producto
) {
    var expandido by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { expandido = !expandido },
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
                    .padding(16.dp)
            ) {

                // Fila superior con imagen y nombre
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen del producto
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(producto.color.copy(alpha = 0.2f))
                            .border(
                                1.dp,
                                producto.color.copy(alpha = 0.3f),
                                RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = producto.imagenResId),
                            contentDescription = producto.nombre,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = producto.nombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = producto.tipo,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    // Indicador de expandir
                    Text(
                        text = if (expandido) "▼" else "▶",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Información expandible
                if (expandido) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Divider(
                        color = Color.White.copy(alpha = 0.3f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "📅 Temporada",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "${nombreMes(producto.mesInicio)} - ${nombreMes(producto.mesFin)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "✨ ${producto.beneficio}",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// -------------------- TARJETA SIN PRODUCTOS --------------------
@Composable
fun TarjetaSinProductos() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp, vertical = 8.dp),
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
                .padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "😕 No hay productos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "No hay productos de temporada para este mes con el filtro seleccionado",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -------------------- FUNCIONES AUXILIARES --------------------
fun obtenerMesActual(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.MONTH) + 1
}

fun nombreMes(mes: Int): String {
    return when (mes) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> ""
    }
}