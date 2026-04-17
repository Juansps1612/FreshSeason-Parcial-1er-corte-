package org.proyecto.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.proyecto.project.model.AdminUserDto
import org.proyecto.project.model.AdminUsersListResponse
import org.proyecto.project.model.BasicResponse

@Composable
fun AdminUsuariosScreen(
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val jsonParser = remember {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    val client = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) { json(jsonParser) }
        }
    }

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var users by remember { mutableStateOf<List<AdminUserDto>>(emptyList()) }
    var confirmDelete by remember { mutableStateOf<AdminUserDto?>(null) }
    var deleting by remember { mutableStateOf(false) }

    fun cargar() {
        scope.launch {
            loading = true
            error = null
            try {
                val resp: AdminUsersListResponse =
                    client.get("${AppConfig.API_URL}/admin_users_list.php").body()
                if (resp.success) users = resp.users
                else error = resp.message ?: "No se pudieron cargar los usuarios"
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) { cargar() }

    if (confirmDelete != null) {
        val u = confirmDelete!!
        AlertDialog(
            onDismissRequest = { if (!deleting) confirmDelete = null },
            confirmButton = {
                TextButton(
                    enabled = !deleting,
                    onClick = {
                        scope.launch {
                            deleting = true
                            try {
                                val resp: BasicResponse = client.submitForm(
                                    url = "${AppConfig.API_URL}/admin_users_delete.php",
                                    formParameters = parameters {
                                        append("id", u.id.toString())
                                    }
                                ).body()
                                if (resp.success) {
                                    confirmDelete = null
                                    cargar()
                                } else {
                                    error = resp.message ?: "No se pudo eliminar"
                                    confirmDelete = null
                                }
                            } catch (e: Exception) {
                                error = "Error: ${e.message}"
                                confirmDelete = null
                            } finally {
                                deleting = false
                            }
                        }
                    }
                ) {
                    Text(if (deleting) "Eliminando..." else "Eliminar")
                }
            },
            dismissButton = {
                TextButton(enabled = !deleting, onClick = { confirmDelete = null }) { Text("Cancelar") }
            },
            title = { Text("Eliminar usuario") },
            text = { Text("¿Eliminar a ${u.email ?: "este usuario"}?") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF5CFF5C), Color(0xFF063D06))))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "👑 Admin · Usuarios",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Volver",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onBack() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        if (loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
            return
        }

        error?.let {
            Text(
                text = it,
                color = Color(0xFFFFCDD2),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(users, key = { it.id }) { u ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.18f))
                            .border(
                                1.2.dp,
                                Brush.linearGradient(listOf(Color.White.copy(0.8f), Color.White.copy(0.3f))),
                                RoundedCornerShape(18.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = u.nombre ?: "Sin nombre",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = u.email ?: "—",
                                    color = Color.White.copy(alpha = 0.75f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Membresía: ${u.membresia ?: "—"}",
                                    color = Color.White.copy(alpha = 0.75f),
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "Eliminar",
                                color = Color(0xFFFFCDD2),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(enabled = !deleting) {
                                        if (u.id == SessionManager.userId) {
                                            error = "No puedes eliminar tu propio usuario"
                                        } else {
                                            confirmDelete = u
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

