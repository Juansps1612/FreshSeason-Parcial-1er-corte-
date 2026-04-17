// SessionManager.kt
package org.proyecto.project

object SessionManager {
    var userId: Int = 0
    var nombre: String = ""
    var email: String = ""
    var membresia: String = ""
    var fechaRegistro: String = ""
    var totalFavoritos: Int = 0

    fun cerrarSesion() {
        userId = 0
        nombre = ""
        email = ""
        membresia = ""
        fechaRegistro = ""
    }

    fun estaLogueado() = userId != 0
}
