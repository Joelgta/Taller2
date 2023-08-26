package com.taller2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.taller2.database.Articulo
import com.taller2.database.DatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class Accion { LISTAR, CREAR, EDITAR }

@Composable
fun ListaComprasUI() {
    val contexto = LocalContext.current
    val (articulos, setArticulos) = remember {
        mutableStateOf(
            emptyList<Articulo>()
        )
    }
    val (seleccion, setSeleccion) = remember { mutableStateOf<Articulo?>(null) }
    val (accion, setAccion) = remember {
        mutableStateOf(
            Accion.LISTAR
        )
    }
    LaunchedEffect(articulos) {
        withContext(Dispatchers.IO) {
            val db = DatabaseManager.getDatabase(contexto)
            setArticulos(db.articuloDao().getAll())
        }
    }
    val onSave = {
        setAccion(Accion.LISTAR)
        setArticulos(emptyList())
    }
    when (accion) {
        Accion.CREAR -> ArticuloFormUI(null, onSave)
        Accion.EDITAR -> ArticuloFormUI(seleccion, onSave)
        else -> ListaArticulosUI(
            articulos,
            onAdd = { setAccion(Accion.CREAR) },
            onEdit = { articulo ->
                setSeleccion(articulo)
                setAccion(Accion.EDITAR)
            })
    }
}