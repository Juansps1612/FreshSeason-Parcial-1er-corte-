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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

// -------------------- MODELO DE DATOS --------------------

// -------------------- BUSCAR SCREEN --------------------
// -------------------- BUSCAR SCREEN --------------------
// -------------------- BUSCAR SCREEN --------------------
@Composable
fun BuscarScreen(
    favoritosViewModel: FavoritosViewModel = viewModel()
) {

    var searchText by remember { mutableStateOf("") }
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    var isSearching by remember { mutableStateOf(false) }
    var resultados by remember { mutableStateOf(listOf<ProductoBusqueda>()) }
    var productoSeleccionado by remember { mutableStateOf<ProductoBusqueda?>(null) }

    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    val filtros = listOf("Todos", "Frutas", "Verduras", "Temporada", "Beneficios")

    // Convertir todos los productos a ProductoBusqueda
    val todosLosProductosBusqueda = remember {
        todosLosProductos.map { it.toProductoBusqueda() }
    }

    // Simular búsqueda
    fun buscarProductos(query: String) {
        if (query.isBlank()) {
            resultados = emptyList()
            return
        }

        resultados = todosLosProductosBusqueda.filter { producto ->
            val coincideTexto = producto.nombre.contains(query, ignoreCase = true) ||
                    producto.beneficio.contains(query, ignoreCase = true)

            when (filtroSeleccionado) {
                "Frutas" -> coincideTexto && producto.tipo == "Fruta"
                "Verduras" -> coincideTexto && producto.tipo == "Verdura"
                "Temporada" -> coincideTexto && producto.temporada == obtenerTemporadaActual()
                "Beneficios" -> coincideTexto
                else -> coincideTexto
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // ---------------- BARRA DE BÚSQUEDA ----------------
        Spacer(modifier = Modifier.height(16.dp))

        CustomSearchBar(
            searchText = searchText,
            onSearchTextChange = {
                searchText = it
                isSearching = true
                productoSeleccionado = null
                coroutineScope.launch {
                    delay(300)
                    buscarProductos(it)
                    isSearching = false
                }
            },
            onSearch = {
                focusManager.clearFocus()
                buscarProductos(searchText)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- FILTROS RÁPIDOS ----------------
        FiltrosBusqueda(
            filtros = filtros,
            filtroSeleccionado = filtroSeleccionado,
            onFiltroSelected = {
                filtroSeleccionado = it
                buscarProductos(searchText)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- CONTENIDO PRINCIPAL ----------------
        if (productoSeleccionado != null) {
            ProductoPerfilScreen(
                producto = productoSeleccionado!!,
                isFavorito = favoritosViewModel.isFavorito(productoSeleccionado!!),
                onToggleFavorito = { favoritosViewModel.toggleFavorito(productoSeleccionado!!) },
                onBack = { productoSeleccionado = null }
            )
        } else {
            if (searchText.isBlank()) {
                SugerenciasSection(
                    onSugerenciaClick = { sugerencia ->
                        searchText = sugerencia
                        buscarProductos(sugerencia)
                    }
                )
            } else {
                ResultadosSection(
                    resultados = resultados,
                    isSearching = isSearching,
                    query = searchText,
                    onProductoClick = { producto ->
                        productoSeleccionado = producto
                    }
                )
            }
        }
    }
}

// -------------------- BARRA DE BÚSQUEDA PERSONALIZADA --------------------
@Composable
fun CustomSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(28.dp)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.15f)
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
                RoundedCornerShape(28.dp)
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.lupa),
                contentDescription = "Buscar",
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(Color.White),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Buscar frutas, verduras...",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier.weight(1f)
            )

            if (searchText.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onSearchTextChange("")
                            focusManager.clearFocus()
                        }
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.3f),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// -------------------- FILTROS DE BÚSQUEDA --------------------
@Composable
fun FiltrosBusqueda(
    filtros: List<String>,
    filtroSeleccionado: String,
    onFiltroSelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filtros) { filtro ->
            val isSelected = filtro == filtroSeleccionado

            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
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
                                RoundedCornerShape(18.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable { onFiltroSelected(filtro) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filtro,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

// -------------------- PERFIL DEL PRODUCTO --------------------
@Composable
fun ProductoPerfilScreen(
    producto: ProductoBusqueda,
    isFavorito: Boolean,
    onToggleFavorito: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { onBack() }
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.3f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Detalles del producto",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
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
                        RoundedCornerShape(32.dp)
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(producto.color.copy(alpha = 0.3f))
                            .border(
                                2.dp,
                                producto.color.copy(alpha = 0.5f),
                                RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = producto.imagenResId),
                            contentDescription = producto.nombre,
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = producto.nombre,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(producto.color.copy(alpha = 0.3f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = producto.tipo,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = producto.temporada,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "✨ Beneficio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = producto.beneficio,
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                if (isFavorito) {
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0xFFFF6B6B),
                                            Color(0xFFC62828)
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
                                if (isFavorito) {
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Transparent)
                                    )
                                } else {
                                    Brush.linearGradient(
                                        listOf(
                                            Color.White.copy(alpha = 0.9f),
                                            Color.White.copy(alpha = 0.4f)
                                        )
                                    )
                                },
                                RoundedCornerShape(28.dp)
                            )
                            .clickable { onToggleFavorito() }
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.favorito),
                                contentDescription = null,
                                tint = if (isFavorito) Color.White else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = if (isFavorito) "En favoritos" else "Añadir a favoritos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isFavorito) Color.White else Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------- SECCIÓN DE SUGERENCIAS --------------------
@Composable
fun SugerenciasSection(
    onSugerenciaClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

        Text(
            text = "🔍 Búsquedas populares",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(listOf("Manzana", "Plátano", "Espinaca", "Fresa", "Tomate", "Zanahoria")) { sugerencia ->
                ChipSugerencia(
                    text = sugerencia,
                    onClick = { onSugerenciaClick(sugerencia) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "✨ Beneficios destacados",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn {
            items(listOf(
                "Vitamina C" to "Fortalece el sistema inmunológico",
                "Fibra" to "Mejora la digestión",
                "Antioxidantes" to "Combaten el envejecimiento"
            )) { (titulo, desc) ->
                BeneficioSugerenciaCard(
                    titulo = titulo,
                    descripcion = desc,
                    onClick = { onSugerenciaClick(titulo) }
                )
            }
        }
    }
}

// -------------------- CHIP DE SUGERENCIA --------------------
@Composable
fun ChipSugerencia(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(40.dp)
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
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

// -------------------- TARJETA DE BENEFICIO SUGERIDO --------------------
@Composable
fun BeneficioSugerenciaCard(
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
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
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = descripcion,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// -------------------- SECCIÓN DE RESULTADOS --------------------
@Composable
fun ResultadosSection(
    resultados: List<ProductoBusqueda>,
    isSearching: Boolean,
    query: String,
    onProductoClick: (ProductoBusqueda) -> Unit
) {
    if (isSearching) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (resultados.isEmpty() && query.isNotEmpty()) {
                item {
                    TarjetaSinResultados(query = query)
                }
            } else {
                items(resultados.size) { index ->
                    TarjetaResultadoBusqueda(
                        producto = resultados[index],
                        onClick = { onProductoClick(resultados[index]) }
                    )
                }
            }
        }
    }
}

// -------------------- TARJETA DE RESULTADO DE BÚSQUEDA --------------------
@Composable
fun TarjetaResultadoBusqueda(
    producto: ProductoBusqueda,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 6.dp),
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
                .clickable { onClick() }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(producto.color.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            producto.color.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = producto.tipo,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        Text(
                            text = "•",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )

                        Text(
                            text = producto.temporada,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Text(
                        text = producto.beneficio.take(50) + "...",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1
                    )
                }

                Text(
                    text = "→",
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// -------------------- TARJETA SIN RESULTADOS --------------------
@Composable
fun TarjetaSinResultados(query: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 20.dp),
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
                    1.5.dp,
                    Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.9f),
                            Color.White.copy(alpha = 0.4f)
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "😕 Sin resultados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "No encontramos '$query'",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Text(
                    text = "Prueba con otra búsqueda",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// -------------------- DATOS PARA BÚSQUEDA --------------------
val productosDisponibles = listOf(
    ProductoBusqueda("Fresa", "Fruta", "Primavera", "Rica en vitamina C y antioxidantes", Color(0xFFFF6B6B), R.drawable.fresa),
    ProductoBusqueda("Manzana", "Fruta", "Otoño", "Regula el tránsito intestinal", Color(0xFFEF5350), R.drawable.manzana),
    ProductoBusqueda("Plátano", "Fruta", "Todo el año", "Rico en potasio y energía", Color(0xFFFFD54F), R.drawable.banano),
    ProductoBusqueda("Espinaca", "Verdura", "Primavera", "Fuente de hierro y vitaminas", Color(0xFF43A047), R.drawable.espinaca),
    ProductoBusqueda("Brócoli", "Verdura", "Invierno", "Alto en calcio y antioxidantes", Color(0xFF2E7D32), R.drawable.brocoli),
    ProductoBusqueda("Zanahoria", "Verdura", "Todo el año", "Rica en betacaroteno", Color(0xFFFF9800), R.drawable.zanahoria),
    ProductoBusqueda("Naranja", "Fruta", "Invierno", "Fortalece las defensas", Color(0xFFFFA726), R.drawable.naranja),
    ProductoBusqueda("Tomate", "Verdura", "Verano", "Licopeno antioxidante", Color(0xFFE53935), R.drawable.tomate),
    ProductoBusqueda("Aguacate", "Fruta", "Todo el año", "Grasas saludables", Color(0xFF8D6E63), R.drawable.aguacate),
    ProductoBusqueda("Calabacín", "Verdura", "Verano", "Bajo en calorías", Color(0xFF66BB6A), R.drawable.calabacin)
)

// -------------------- FUNCIÓN AUXILIAR --------------------

