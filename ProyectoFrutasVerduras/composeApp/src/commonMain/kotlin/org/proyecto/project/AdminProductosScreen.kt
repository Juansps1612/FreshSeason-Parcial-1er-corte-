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
import androidx.compose.material3.OutlinedTextField
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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.proyecto.project.model.AdminProductoDto
import org.proyecto.project.model.AdminProductosListResponse
import org.proyecto.project.model.BasicResponse

@Composable
fun AdminProductosScreen(
    onBack: () -> Unit,
    productosViewModel: ProductosViewModel? = null
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
    var productos by remember { mutableStateOf<List<AdminProductoDto>>(emptyList()) }

    var editorVisible by remember { mutableStateOf(false) }
    var editando by remember { mutableStateOf<AdminProductoDto?>(null) }
    var confirmDelete by remember { mutableStateOf<AdminProductoDto?>(null) }
    var saving by remember { mutableStateOf(false) }

    // Campos editor
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Fruta") }
    var temporada by remember { mutableStateOf("") }
    var mesInicio by remember { mutableStateOf("") }
    var mesFin by remember { mutableStateOf("") }
    var beneficio by remember { mutableStateOf("") }

    fun abrirEditor(p: AdminProductoDto?) {
        editando = p
        nombre = p?.nombre ?: ""
        tipo = p?.tipo ?: "Fruta"
        temporada = p?.temporada ?: ""
        mesInicio = p?.mesInicio?.toString() ?: ""
        mesFin = p?.mesFin?.toString() ?: ""
        beneficio = p?.beneficio ?: ""
        editorVisible = true
    }

    fun cargar() {
        scope.launch {
            loading = true
            error = null
            try {
                val resp: AdminProductosListResponse =
                    client.get("${AppConfig.API_URL}/admin_products_list.php").body()
                if (resp.success) productos = resp.productos
                else error = resp.message ?: "No se pudieron cargar los productos"
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    fun guardar() {
        val id = editando?.id
        val endpoint = if (id == null) "admin_products_create.php" else "admin_products_update.php"

        scope.launch {
            saving = true
            error = null
            try {
                val raw: String = client.submitForm(
                    url = "${AppConfig.API_URL}/$endpoint",
                    formParameters = parameters {
                        if (id != null) append("id", id.toString())
                        append("nombre", nombre.trim())
                        append("tipo", tipo.trim())
                        append("temporada", temporada.trim())
                        append("mes_inicio", mesInicio.trim())
                        append("mes_fin", mesFin.trim())
                        append("beneficio", beneficio.trim())
                    }
                ).bodyAsText()

                val resp = runCatching {
                    val el = jsonParser.parseToJsonElement(raw)
                    jsonParser.decodeFromJsonElement<BasicResponse>(el)
                }.getOrNull()

                if (resp?.success == true) {
                    editorVisible = false
                    editando = null
                    cargar()
                    productosViewModel?.refrescar()
                } else {
                    val serverMsg = resp?.message
                    error = serverMsg
                        ?: "Respuesta no válida del servidor:\n" + raw.take(300)
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                saving = false
            }
        }
    }

    fun eliminar(p: AdminProductoDto) {
        scope.launch {
            saving = true
            error = null
            try {
                val resp: BasicResponse = client.submitForm(
                    url = "${AppConfig.API_URL}/admin_products_delete.php",
                    formParameters = parameters { append("id", p.id.toString()) }
                ).body()
                if (resp.success) {
                    confirmDelete = null
                    cargar()
                    productosViewModel?.refrescar()
                } else {
                    error = resp.message ?: "No se pudo eliminar"
                    confirmDelete = null
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
                confirmDelete = null
            } finally {
                saving = false
            }
        }
    }

    LaunchedEffect(Unit) { cargar() }

    if (editorVisible) {
        AlertDialog(
            onDismissRequest = { if (!saving) editorVisible = false },
            title = { Text(if (editando == null) "Agregar producto" else "Editar producto") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = { tipo = it },
                        label = { Text("Tipo (Fruta/Verdura)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = temporada,
                        onValueChange = { temporada = it },
                        label = { Text("Temporada (opcional)") },
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = mesInicio,
                            onValueChange = { mesInicio = it },
                            label = { Text("Mes inicio") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = mesFin,
                            onValueChange = { mesFin = it },
                            label = { Text("Mes fin") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = beneficio,
                        onValueChange = { beneficio = it },
                        label = { Text("Beneficio (opcional)") }
                    )
                }
            },
            confirmButton = {
                TextButton(enabled = !saving, onClick = { guardar() }) {
                    Text(if (saving) "Guardando..." else "Guardar")
                }
            },
            dismissButton = {
                TextButton(enabled = !saving, onClick = { editorVisible = false }) { Text("Cancelar") }
            }
        )
    }

    if (confirmDelete != null) {
        val p = confirmDelete!!
        AlertDialog(
            onDismissRequest = { if (!saving) confirmDelete = null },
            confirmButton = {
                TextButton(enabled = !saving, onClick = { eliminar(p) }) {
                    Text(if (saving) "Eliminando..." else "Eliminar")
                }
            },
            dismissButton = {
                TextButton(enabled = !saving, onClick = { confirmDelete = null }) { Text("Cancelar") }
            },
            title = { Text("Eliminar producto") },
            text = { Text("¿Eliminar ${p.nombre}?") }
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
                text = "👑 Admin · Productos",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Agregar",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.18f))
                    .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                    .clickable(enabled = !saving) { abrirEditor(null) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
            Spacer(Modifier.width(8.dp))
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
            items(productos, key = { it.id }) { p ->
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
                                Text(text = p.nombre, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "${p.tipo} · ${p.temporada ?: "—"}",
                                    color = Color.White.copy(alpha = 0.75f),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                p.beneficio?.takeIf { it.isNotBlank() }?.let { b ->
                                    Text(
                                        text = b,
                                        color = Color.White.copy(alpha = 0.75f),
                                        fontSize = 12.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Text(
                                text = "Editar",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(enabled = !saving) { abrirEditor(p) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Eliminar",
                                color = Color(0xFFFFCDD2),
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(enabled = !saving) { confirmDelete = p }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

