package com.taller2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taller2.database.Articulo
import com.taller2.database.DatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ArticuloItemUI(articulo: Articulo, onClick: () -> Unit = {}) {
    val (estado, setEstado) = remember { mutableStateOf(articulo.estado) }
    val contexto = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier
            .weight(1f)
            .clickable { onClick() }) {
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.shopping_cart),
                contentDescription = "Imagen Contacto"
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                articulo.nombre?.let {
                    Text(
                        it,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Checkbox(checked = estado!!, onCheckedChange = {
                setEstado(!estado)
                coroutineScope.launch(Dispatchers.IO)
                {
                    val dao = DatabaseManager.getDatabase(contexto).articuloDao()
                    val articuloDB = Articulo(articulo.id, articulo.nombre, !estado)
                    dao.update(articuloDB)
                }
            })
        }
    }
}