package org.proyecto.project.model

data class LoginResponse(
    val success: Boolean = false,
    val user: Usuario? = null,
    val message: String? = null
)

data class Usuario(
    val id: Int? = null,
    val nombre: String? = null,
    val email: String? = null,
    val membresia: String? = null
)