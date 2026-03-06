package org.proyecto.project

import androidx.compose.ui.graphics.Color
import java.util.Calendar

// -------------------- MODELO DE DATOS COMPARTIDO --------------------
data class Producto(
    val nombre: String,
    val tipo: String, // "Fruta" o "Verdura"
    val temporada: String,
    val mesInicio: Int,
    val mesFin: Int,
    val color: Color,
    val beneficio: String,
    val imagenResId: Int
)

// -------------------- MODELO PARA BÚSQUEDA --------------------
data class ProductoBusqueda(
    val nombre: String,
    val tipo: String,
    val temporada: String,
    val beneficio: String,
    val color: Color,
    val imagenResId: Int
)

// -------------------- FUNCIÓN DE CONVERSIÓN --------------------
fun Producto.toProductoBusqueda(): ProductoBusqueda {
    return ProductoBusqueda(
        nombre = this.nombre,
        tipo = this.tipo,
        temporada = this.temporada,
        beneficio = this.beneficio,
        color = this.color,
        imagenResId = this.imagenResId
    )
}

// -------------------- FUNCIONES AUXILIARES --------------------
fun obtenerTemporadaActual(): String {
    val mes = Calendar.getInstance().get(Calendar.MONTH) + 1
    return when (mes) {
        in 3..5 -> "Primavera"
        in 6..8 -> "Verano"
        in 9..11 -> "Otoño"
        else -> "Invierno"
    }
}

fun obtenerTemporadaPorMes(mes: Int): String {
    return when (mes) {
        in 3..5 -> "Primavera"
        in 6..8 -> "Verano"
        in 9..11 -> "Otoño"
        else -> "Invierno"
    }
}

// -------------------- DATOS COMPLETOS DE TODOS LOS PRODUCTOS --------------------
val todosLosProductos = listOf(
    // FRUTAS
    Producto("Fresa", "Fruta", "Primavera", 4, 6, Color(0xFFFF6B6B),
        "Rica en vitamina C y antioxidantes, favorece la salud cardiovascular", R.drawable.fresa),
    Producto("Cereza", "Fruta", "Primavera", 5, 7, Color(0xFFB71C1C),
        "Ayuda a combatir el insomnio, rica en melatonina natural", R.drawable.cerezas),
    Producto("Naranja", "Fruta", "Invierno", 11, 3, Color(0xFFFFA726),
        "Fortalece el sistema inmunológico, alta en vitamina C", R.drawable.naranja),
    Producto("Manzana", "Fruta", "Otoño", 9, 11, Color(0xFFEF5350),
        "Regula el tránsito intestinal, rica en pectina y fibra", R.drawable.manzana),
    Producto("Melón", "Fruta", "Verano", 7, 9, Color(0xFFFFB74D),
        "Hidratante, rico en betacaroteno y vitamina A", R.drawable.melon),
    Producto("Frambuesa", "Fruta", "Verano", 6, 8, Color(0xFFE91E63),
        "Poderoso antioxidante, rica en manganeso y vitamina C", R.drawable.frambuesas),
    Producto("Limón", "Fruta", "Todo el año", 1, 12, Color(0xFFFFEE58),
        "Alcalinizante, digestivo y rico en vitamina C", R.drawable.limon),
    Producto("Mandarina", "Fruta", "Invierno", 10, 1, Color(0xFFFFB74D),
        "Fuente de vitamina C, antioxidante y baja en calorías", R.drawable.mandarina),
    Producto("Pera", "Fruta", "Otoño", 8, 10, Color(0xFFCDDC39),
        "Rica en fibra, potasio y favorece la digestión", R.drawable.pera),
    Producto("Sandía", "Fruta", "Verano", 6, 8, Color(0xFFF44336),
        "Hidratante, rica en licopeno y baja en calorías", R.drawable.sandia),
    Producto("Kiwi", "Fruta", "Invierno", 10, 1, Color(0xFF8BC34A),
        "Más vitamina C que la naranja, rico en fibra", R.drawable.kiwi),
    Producto("Plátano", "Fruta", "Todo el año", 1, 12, Color(0xFFFFD54F),
        "Rico en potasio, magnesio y energía natural", R.drawable.banano),
    Producto("Pomelo", "Fruta", "Invierno", 11, 3, Color(0xFFFF8A65),
        "Depurativo, ayuda a reducir el colesterol", R.drawable.pomelo),

    // VERDURAS
    Producto("Espárrago", "Verdura", "Primavera", 3, 5, Color(0xFF4CAF50),
        "Diurético, rico en fibra, ácido fólico y vitamina E", R.drawable.esparrago),
    Producto("Calabacín", "Verdura", "Verano", 6, 8, Color(0xFF66BB6A),
        "Bajo en calorías, rico en potasio y vitamina C", R.drawable.calabacin),
    Producto("Brócoli", "Verdura", "Invierno", 10, 4, Color(0xFF2E7D32),
        "Alto contenido en calcio, hierro y sulforafano", R.drawable.brocoli),
    Producto("Espinaca", "Verdura", "Primavera", 3, 5, Color(0xFF43A047),
        "Fuente de hierro, magnesio, vitaminas A, C y K", R.drawable.espinaca),
    Producto("Tomate", "Verdura", "Verano", 6, 9, Color(0xFFE53935),
        "Licopeno, poderoso antioxidante, rico en vitamina A", R.drawable.tomate),
    Producto("Pimiento", "Verdura", "Verano", 7, 9, Color(0xFFF44336),
        "Alto contenido en vitamina C y capsaicina", R.drawable.pimiento),
    Producto("Zanahoria", "Verdura", "Todo el año", 1, 12, Color(0xFFFF9800),
        "Rica en betacaroteno, buena para la vista y piel", R.drawable.zanahoria),
    Producto("Aguacate", "Fruta", "Todo el año", 1, 12, Color(0xFF8D6E63),
        "Grasas saludables, rico en vitamina E y potasio", R.drawable.aguacate),
    Producto("Coliflor", "Verdura", "Invierno", 10, 12, Color(0xFFF5F5F5),
        "Rica en vitamina C, K y compuestos antioxidantes", R.drawable.coliflor),
    Producto("Remolacha", "Verdura", "Invierno", 11, 1, Color(0xFFC2185B),
        "Depurativa, rica en hierro y antioxidantes", R.drawable.remolacha),
    Producto("Calabaza", "Verdura", "Otoño", 9, 11, Color(0xFFFF9800),
        "Rica en vitamina A, betacarotenos y fibra", R.drawable.calabaza),
    Producto("Setas", "Verdura", "Otoño", 9, 11, Color(0xFFBCAAA4),
        "Fuente de vitamina D, selenio y proteínas", R.drawable.champinones)
)

