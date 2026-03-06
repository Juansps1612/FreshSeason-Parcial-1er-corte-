package org.proyecto.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel

// Importar ProductoPerfilScreen desde BuscarScreen
import org.proyecto.project.ProductoPerfilScreen

@Composable
fun FavoritosScreen(
    favoritosViewModel: FavoritosViewModel = viewModel()
) {
    val favoritos = favoritosViewModel.favoritos
    var productoSeleccionado by remember { mutableStateOf<ProductoBusqueda?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "❤️ Mis favoritos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(24.dp)
        )

        if (productoSeleccionado != null) {
            // Mostrar perfil del producto favorito
            ProductoPerfilScreen(
                producto = productoSeleccionado!!,
                isFavorito = true, // Siempre true porque está en favoritos
                onToggleFavorito = {
                    favoritosViewModel.toggleFavorito(productoSeleccionado!!)
                    productoSeleccionado = null // Volver a la lista después de quitar de favoritos
                },
                onBack = { productoSeleccionado = null }
            )
        } else {
            if (favoritos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
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
                                    text = "😕 No hay favoritos",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Explora y añade productos a tus favoritos",
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    items(favoritos) { producto ->
                        FavoritoCard(
                            producto = producto,
                            onClick = { productoSeleccionado = producto },
                            onRemoveClick = { favoritosViewModel.toggleFavorito(producto) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritoCard(
    producto: ProductoBusqueda,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 6.dp)
            .clickable { onClick() }, // Toda la tarjeta es clickable para ver perfil
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
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen
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

                // Información
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = producto.nombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = producto.tipo,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Botón eliminar (ahora es independiente del click de la tarjeta)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable {
                            onRemoveClick()
                            // No cerrar el perfil aquí, eso lo maneja el ViewModel
                        }
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.3f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}