package org.proyecto.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminUserDto(
    val id: Int,
    val nombre: String? = null,
    val email: String? = null,
    val membresia: String? = null,
    val activo: Int? = null,
    @SerialName("fecha_registro")
    val fechaRegistro: String? = null
)

@Serializable
data class AdminUsersListResponse(
    val success: Boolean = false,
    val users: List<AdminUserDto> = emptyList(),
    val message: String? = null
)

@Serializable
data class BasicResponse(
    val success: Boolean = false,
    val message: String? = null
)

