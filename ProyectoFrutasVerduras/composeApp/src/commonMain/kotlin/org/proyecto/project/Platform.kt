package org.proyecto.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform