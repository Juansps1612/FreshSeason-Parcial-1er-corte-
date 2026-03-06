package org.proyecto.project

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

// ✅ IMPORTAR de Data.kt
import org.proyecto.project.ProductoBusqueda

class FavoritosViewModel : ViewModel() {
    private val _favoritos = mutableStateListOf<ProductoBusqueda>()
    val favoritos: List<ProductoBusqueda> = _favoritos

    fun toggleFavorito(producto: ProductoBusqueda) {
        if (_favoritos.contains(producto)) {
            _favoritos.remove(producto)
        } else {
            _favoritos.add(producto)
        }
    }

    fun isFavorito(producto: ProductoBusqueda): Boolean {
        return _favoritos.contains(producto)
    }
}