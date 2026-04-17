package org.proyecto.project

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parameters
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<ProductoBusqueda>()
    val favoritos: List<ProductoBusqueda> = _favoritos

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val client = HttpClient(CIO)

    private val baseUrl = "${AppConfig.API_URL}/favoritos"

    private fun leerEntero(obj: JsonObject, vararg claves: String): Int? {
        for (c in claves) {
            when (val v = obj[c]) {
                is JsonPrimitive -> {
                    v.intOrNull?.let { return it }
                    v.content.toIntOrNull()?.let { return it }
                }
                else -> {}
            }
        }
        return null
    }

    private fun parseIdsDesdeListadoFavoritos(raw: String): List<Int> {
        return try {
            when (val root = jsonParser.parseToJsonElement(raw)) {
                is JsonArray -> root.mapNotNull { el ->
                    (el as? JsonObject)?.let { obj ->
                        leerEntero(obj, "producto_id", "id_producto", "product_id")
                            ?: leerEntero(obj, "id")
                    }
                }
                is JsonObject -> {
                    val arr = listOf("data", "favoritos", "results", "items", "rows")
                        .firstNotNullOfOrNull { key -> root[key] as? JsonArray }
                        ?: return emptyList()
                    arr.mapNotNull { el ->
                        (el as? JsonObject)?.let { obj ->
                            leerEntero(obj, "producto_id", "id_producto", "product_id")
                                ?: leerEntero(obj, "id")
                        }
                    }
                }
                else -> emptyList()
            }
        } catch (e: Exception) {
            println("parseIdsDesdeListadoFavoritos: ${e.message}")
            emptyList()
        }
    }

    fun setUser(userId: Int) {
        SessionManager.userId = userId
        if (userId == 0) {
            _favoritos.clear()
            SessionManager.totalFavoritos = 0
            return
        }
        _favoritos.clear()
        cargarFavoritos()
    }

    fun cargarFavoritos() {
        val userId = SessionManager.userId
        if (userId == 0) return

        viewModelScope.launch {
            try {
                _favoritos.clear()

                val raw = client.get("$baseUrl/list.php?usuario_id=$userId").bodyAsText()
                val idsFavoritos = parseIdsDesdeListadoFavoritos(raw)

                val productosEncontrados = todosLosProductos
                    .filter { it.id in idsFavoritos }
                    .map { it.toProductoBusqueda() }

                _favoritos.addAll(productosEncontrados)
                SessionManager.totalFavoritos = _favoritos.size

                println("Favoritos cargados: ${_favoritos.size} usuario=$userId ids=$idsFavoritos")
            } catch (e: Exception) {
                println("Error en cargarFavoritos: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorito(producto: ProductoBusqueda) {
        val userId = SessionManager.userId
        if (userId == 0) return

        viewModelScope.launch {
            try {
                if (isFavorito(producto)) {
                    val res = client.submitForm(
                        url = "$baseUrl/remove.php",
                        formParameters = parameters {
                            append("usuario_id", userId.toString())
                            append("producto_id", producto.id.toString())
                        }
                    )
                    if (res.status.value in 200..299) {
                        _favoritos.removeAll { it.id == producto.id }
                    } else {
                        println("remove favorito HTTP ${res.status}")
                    }
                } else {
                    val res = client.submitForm(
                        url = "$baseUrl/add.php",
                        formParameters = parameters {
                            append("usuario_id", userId.toString())
                            append("producto_id", producto.id.toString())
                        }
                    )
                    if (res.status.value in 200..299) {
                        _favoritos.add(producto)
                    } else {
                        println("add favorito HTTP ${res.status}")
                    }
                }
                SessionManager.totalFavoritos = _favoritos.size
            } catch (e: Exception) {
                println("Error al toggle favorito: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun isFavorito(producto: ProductoBusqueda): Boolean {
        return _favoritos.any { it.id == producto.id }
    }

    fun limpiarFavoritos() {
        _favoritos.clear()
        SessionManager.totalFavoritos = 0
    }
}
