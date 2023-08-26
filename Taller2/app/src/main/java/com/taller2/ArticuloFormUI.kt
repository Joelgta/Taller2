package com.taller2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.taller2.database.Articulo
import com.taller2.database.DatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticuloFormUI(articulo: Articulo?, onSave: () -> Unit = {}) {
    val contexto = LocalContext.current
    val (nombre, setNombre) = remember {
        mutableStateOf(
            articulo?.nombre ?: ""
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var articuloExistText by remember { mutableStateOf("") }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(articuloExistText)
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = nombre,
                onValueChange = { setNombre(it) },
                label = {
                    Text("Nombre")
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                coroutineScope.launch(Dispatchers.IO)
                {
                    val dao = DatabaseManager.getDatabase(contexto).articuloDao()
                    val articuloDB = Articulo(articulo?.id ?: 0, nombre, articulo?.estado ?: false)
                    val articuloNombreDB = dao.findByName(nombre)
                    if (articuloNombreDB != null) {
                        articuloExistText =
                            "Ya existe '${articuloDB.nombre}' en la lista de compras"
                        return@launch
                    }
                    articuloExistText = ""
                    if (articuloDB.id > 0) {
                        dao.update(articuloDB)
                    } else {
                        dao.insert(articuloDB)
                    }
                    snackbarHostState.showSnackbar("Se ha guardado ${articuloDB.nombre}")
                    onSave()
                }
            }) {
                var textoGuardar = "Crear"
                if ((articulo?.id ?: 0) > 0) {
                    textoGuardar = "Guardar"
                }
                Text(textoGuardar)
            }
            if ((articulo?.id ?: 0) > 0) {
                Button(onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        val dao = DatabaseManager.getDatabase(contexto).articuloDao()
                        snackbarHostState.showSnackbar("Eliminando el articulo ${articulo?.nombre}")
                        if (articulo != null) {
                            dao.delete(articulo)
                        }
                        onSave()
                    }
                }) {
                    Text("Eliminar")
                }
            }
            Button(onClick = {
                onSave()
            }) {
                Text("Volver")
            }
        }
    }
}

