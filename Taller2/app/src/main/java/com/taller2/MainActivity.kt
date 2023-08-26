package com.taller2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.taller2.database.Articulo
import com.taller2.database.DatabaseManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationContext.deleteDatabase("lista-compras")
        val articuloDao = DatabaseManager.getDatabase(applicationContext).articuloDao()
        val listaArticulos = listOf(
            Articulo(0, nombre = "Harina", estado = false),
            Articulo(0, nombre = "Leche", estado = false),
            Articulo(0, nombre = "Huevos", estado = false)
        )
        articuloDao.insertMultipleArticulos(listaArticulos)
        setContent { ListaComprasUI() }
    }
}