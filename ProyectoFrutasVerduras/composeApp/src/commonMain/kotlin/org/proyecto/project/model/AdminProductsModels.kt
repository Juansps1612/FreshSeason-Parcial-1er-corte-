package org.proyecto.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminProductoDto(
    val id: Int,
    val nombre: String,
    val tipo: String, // "Fruta" o "Verdura"
    val temporada: String? = null,
    @SerialName("mes_inicio")
    val mesInicio: Int? = null,
    @SerialName("mes_fin")
    val mesFin: Int? = null,
    val beneficio: String? = null
)

@Serializable
data class AdminProductosListResponse(
    val success: Boolean = false,
    val productos: List<AdminProductoDto> = emptyList(),
    val message: String? = null
)

