package org.proyecto.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// -------------------- MODELO DE DATOS --------------------
data class OpcionConfiguracion(
    val titulo: String,
    val icono: Int,
    val ruta: String? = null,
    val esSwitch: Boolean = false,
    val valorSwitch: Boolean = false
)

// -------------------- PERFIL SCREEN --------------------
@Composable
fun PerfilScreen(
    onLogout: () -> Unit = {},
    onEditClick: () -> Unit = {},
    totalFavoritos: Int
) {

    val opcionesConfig = remember {
        mutableStateListOf(
            OpcionConfiguracion("Cerrar sesión", R.drawable.salir, "logout")
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { PerfilPrincipalCard(onEditClick = onEditClick) }
            item { InfoBasicaCard(totalFavoritos = totalFavoritos) }
            item {
                Text(
                    text = "⚙️ Configuración",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(opcionesConfig.size) { index ->
                ConfiguracionItem(
                    opcion = opcionesConfig[index],
                    onSwitchChanged = { nuevoValor ->
                        opcionesConfig[index] = opcionesConfig[index].copy(valorSwitch = nuevoValor)
                    },
                    onLogout = onLogout
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun PerfilPrincipalCard(onEditClick: () -> Unit = {}) {
    val nombre = SessionManager.nombre
    val email = SessionManager.email
    val membresia = SessionManager.membresia

    val iniciales = remember(nombre) {
        nombre.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "US" }
    }

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.verticalGradient(listOf(Color.White.copy(0.28f), Color.White.copy(0.18f))))
                .border(2.dp, Brush.linearGradient(listOf(Color.White.copy(0.9f), Color.White.copy(0.4f))), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .border(2.dp, Color.White.copy(0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = iniciales, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = email, fontSize = 14.sp, color = Color.White.copy(0.7f))

                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF4CAF50).copy(0.3f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Membresía $membresia", fontSize = 10.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                    }
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(0.2f))
                        .border(1.dp, Color.White.copy(0.3f), RoundedCornerShape(10.dp))
                        .clickable { onEditClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.editar_usuario),
                        contentDescription = "Editar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoBasicaCard(totalFavoritos: Int) {
    val fechaRegistro = SessionManager.fechaRegistro

    val fechaFormateada = remember(fechaRegistro) {
        try {
            val partes = fechaRegistro.split("-")
            val meses = listOf("Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic")
            val mesIdx = partes[1].toInt() - 1
            "${meses[mesIdx]} ${partes[0]}"
        } catch (e: Exception) { "—" }
    }

    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.verticalGradient(listOf(Color.White.copy(0.28f), Color.White.copy(0.18f))))
                .border(2.dp, Brush.linearGradient(listOf(Color.White.copy(0.9f), Color.White.copy(0.4f))), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painterResource(R.drawable.favorito), null, tint = Color(0xFFEF5350), modifier = Modifier.size(24.dp))
                    Text(text = totalFavoritos.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Favoritos", fontSize = 12.sp, color = Color.White.copy(0.7f))
                }
                Box(modifier = Modifier.width(1.dp).height(60.dp).background(Color.White.copy(0.2f)))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painterResource(R.drawable.calendario), null, tint = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
                    Text(text = fechaFormateada, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Miembro desde", fontSize = 12.sp, color = Color.White.copy(0.7f))
                }
            }
        }
    }
}

@Composable
fun ConfiguracionItem(
    opcion: OpcionConfiguracion,
    onSwitchChanged: (Boolean) -> Unit,
    onLogout: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !opcion.esSwitch) {
                if (opcion.ruta == "logout") {
                    SessionManager.cerrarSesion()
                    onLogout()
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(listOf(Color.White.copy(0.28f), Color.White.copy(0.18f))))
                .border(1.5.dp, Brush.linearGradient(listOf(Color.White.copy(0.9f), Color.White.copy(0.4f))), RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color.White.copy(0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(opcion.icono), null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = opcion.titulo, fontSize = 16.sp, color = Color.White, modifier = Modifier.weight(1f))
            }
        }
    }
}