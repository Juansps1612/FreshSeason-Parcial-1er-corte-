package org.proyecto.project

import androidx.activity.result.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import kotlinx.coroutines.launch

@Composable
fun EditarPerfilScreen(onBack: () -> Unit) {
    var nuevoNombre by remember { mutableStateOf(SessionManager.nombre) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val client = remember { HttpClient(CIO) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Editar Perfil", style = MaterialTheme.typography.headlineMedium, color = androidx.compose.ui.graphics.Color.White)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = nuevoNombre,
            onValueChange = { nuevoNombre = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            enabled = !loading,
            onClick = {
                scope.launch {
                    loading = true
                    try {
                        // 1. Enviar al servidor PHP
                        val response: String = client.submitForm(
                            url = "http://192.168.1.7/freshseason_api/update_profile.php",
                            formParameters = parameters {
                                append("id", SessionManager.userId.toString())
                                append("nombre", nuevoNombre)
                            }
                        ).body()

                        // 2. Si el servidor respondió bien, actualizamos la memoria local
                        SessionManager.nombre = nuevoNombre
                        onBack()

                    } catch (e: Exception) {
                        println("Error al actualizar: ${e.message}")
                    } finally {
                        loading = false
                    }
                }
            }
        ) {
            if (loading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text("Guardar Cambios")
        }
    }
    }
