package com.taller2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.taller2.database.Articulo
import com.taller2.database.DatabaseManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaArticulosUI(
    articulos: List<Articulo>, onAdd: () -> Unit = {}, onEdit: (articulo: Articulo) -> Unit = {}
) {
    val contexto = LocalContext.current
    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = { onAdd() },
            icon = { Icon(Icons.Filled.Add, contentDescription = "agregar") },
            text = { Text("Agregar") })
    }) { contentPadding ->
        if (articulos.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(articulos) { articulo ->
                    ArticuloItemUI(
                        articulo
                    ) { onEdit(articulo) }
                }
            }
        } else {
            val db = DatabaseManager.getDatabase(contexto)
            val articulosDB = db.articuloDao().getAll()
            if (articulosDB.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(articulosDB) { articulo ->
                        ArticuloItemUI(
                            articulo
                        ) { onEdit(articulo) }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) { Text("Lista de compras vacía.") }
            }
        }
    }
}