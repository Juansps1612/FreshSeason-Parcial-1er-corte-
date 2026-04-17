package org.proyecto.project.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val success: Boolean = false,
    val message: String? = null
)
