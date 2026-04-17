// AppConfig.kt
package org.proyecto.project

object AppConfig {
    // Cambia solo esta línea según donde pruebes:
    private const val BASE_URL = "http://192.168.1.7" // ← tu IP WiFi real

    // private const val BASE_URL = "http://10.0.2.2"  // ← emulador

    const val API_URL = "$BASE_URL/freshseason_api"
}