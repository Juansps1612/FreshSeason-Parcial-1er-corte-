package org.proyecto.project

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<ProductoBusqueda>()
    val favoritos: List<ProductoBusqueda> = _favoritos

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // Ajusta la URL según tu servidor
    private val baseUrl = "http://192.168.1.7/freshseason_api/favoritos"

    fun setUser(userId: Int) {
        if (SessionManager.userId != userId) {
            SessionManager.userId = userId
            _favoritos.clear()
            cargarFavoritos()
        }
    }

    fun cargarFavoritos() {
        val userId = SessionManager.userId
        if (userId == 0) return

        viewModelScope.launch {
            try {
                // Limpiamos antes de cargar para no duplicar
                _favoritos.clear()

                val response: List<JsonObject> = client.get("$baseUrl/list.php?usuario_id=$userId").body()

                // Extraemos los IDs (probamos con "id" y con "producto_id" por si acaso)
                val idsFavoritos = response.mapNotNull {
                    it["producto_id"]?.jsonPrimitive?.intOrNull
                }

                val productosEncontrados = todosLosProductos
                    .filter { it.id in idsFavoritos }
                    .map { it.toProductoBusqueda() }

                _favoritos.addAll(productosEncontrados)
                SessionManager.totalFavoritos = _favoritos.size

                println("✅ Favoritos cargados: ${_favoritos.size} para el usuario $userId")

            } catch (e: Exception) {
                println("❌ Error en cargarFavoritos: ${e.message}")
            }
        }
    }

    fun toggleFavorito(producto: ProductoBusqueda) {
        val userId = SessionManager.userId
        if (userId == 0) return

        viewModelScope.launch {
            try {
                if (isFavorito(producto)) {
                    // Llamar a remove.php
                    client.submitForm(
                        url = "$baseUrl/remove.php",
                        formParameters = parameters {
                            append("usuario_id", userId.toString())
                            append("producto_id", producto.id.toString())
                        }
                    )
                    _favoritos.removeAll { it.id == producto.id }
                } else {
                    // Llamar a add.php
                    client.submitForm(
                        url = "$baseUrl/add.php",
                        formParameters = parameters {
                            append("usuario_id", userId.toString())
                            append("producto_id", producto.id.toString())
                        }
                    )
                    _favoritos.add(producto)
                }
                SessionManager.totalFavoritos = _favoritos.size
            } catch (e: Exception) {
                println("❌ Error al toggle favorito: ${e.message}")
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
