package org.proyecto.project

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.proyecto.project.model.AdminProductoDto
import org.proyecto.project.model.AdminProductosListResponse

class ProductosViewModel : ViewModel() {
    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json(jsonParser) }
    }

    private val _productos = mutableStateListOf<Producto>()
    val productos: List<Producto> = _productos

    var lastError: String? = null
        private set

    fun refrescar() {
        viewModelScope.launch {
            lastError = null
            try {
                val resp: AdminProductosListResponse =
                    client.get("${AppConfig.API_URL}/products_list.php").body()
                if (resp.success) {
                    _productos.clear()
                    _productos.addAll(resp.productos.map { it.toProductoLocal() })
                } else {
                    lastError = resp.message ?: "No se pudieron cargar los productos"
                }
            } catch (e: Exception) {
                lastError = e.message
            }
        }
    }
}

private fun AdminProductoDto.toProductoLocal(): Producto {
    val nombreLower = nombre.trim().lowercase()
    val imagen = when (nombreLower) {
        "fresa" -> R.drawable.fresa
        "cereza" -> R.drawable.cerezas
        "naranja" -> R.drawable.naranja
        "manzana" -> R.drawable.manzana
        "melón", "melon" -> R.drawable.melon
        "frambuesa" -> R.drawable.frambuesas
        "limón", "limon" -> R.drawable.limon
        "mandarina" -> R.drawable.mandarina
        "pera" -> R.drawable.pera
        "sandía", "sandia" -> R.drawable.sandia
        "kiwi" -> R.drawable.kiwi
        "plátano", "platano" -> R.drawable.banano
        "pomelo" -> R.drawable.pomelo
        "espárrago", "esparrago" -> R.drawable.esparrago
        "calabacín", "calabacin" -> R.drawable.calabacin
        "brócoli", "brocoli" -> R.drawable.brocoli
        "espinaca" -> R.drawable.espinaca
        "tomate" -> R.drawable.tomate
        "pimiento" -> R.drawable.pimiento
        "zanahoria" -> R.drawable.zanahoria
        "aguacate" -> R.drawable.aguacate
        "coliflor" -> R.drawable.coliflor
        "remolacha" -> R.drawable.remolacha
        "calabaza" -> R.drawable.calabaza
        "setas", "champiñones", "champinones" -> R.drawable.champinones
        else -> R.drawable.fruta
    }

    val color = when (tipo.trim().lowercase()) {
        "verdura" -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
        else -> androidx.compose.ui.graphics.Color(0xFFFFB74D)
    }

    val mi = (mesInicio ?: 1).coerceIn(1, 12)
    val mf = (mesFin ?: 12).coerceIn(1, 12)

    return Producto(
        id = id,
        nombre = nombre,
        tipo = tipo,
        temporada = temporada ?: "",
        mesInicio = mi,
        mesFin = mf,
        color = color,
        beneficio = beneficio ?: "",
        imagenResId = imagen
    )
}

