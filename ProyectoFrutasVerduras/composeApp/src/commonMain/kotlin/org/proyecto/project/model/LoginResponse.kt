package org.proyecto.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class LoginResponse(
    val success: Boolean = false,
    val user: Usuario? = null,
    val message: String? = null
)

@Serializable
data class Usuario(
    @JsonNames("usuario_id", "user_id", "ID")
    val id: Int? = null,
    val nombre: String? = null,
    val email: String? = null,
    val membresia: String? = null,
    @SerialName("fecha_registro")
    val fechaRegistro: String? = null
)