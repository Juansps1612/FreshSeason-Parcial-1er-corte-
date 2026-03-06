package org.proyecto.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import proyectofrutasverduras.composeapp.generated.resources.*

@Composable
fun InterFont(): FontFamily {
    return FontFamily(
        Font(Res.font.inter_regular, weight = FontWeight.Normal),
        Font(Res.font.inter_medium, weight = FontWeight.Medium),
        Font(Res.font.inter_semibold, weight = FontWeight.SemiBold),
        Font(Res.font.inter_bold, weight = FontWeight.Bold),
    )
}