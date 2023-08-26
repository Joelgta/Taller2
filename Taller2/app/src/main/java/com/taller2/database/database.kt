package com.taller2.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class Articulo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String?,
    @ColumnInfo(name = "estado") val estado: Boolean?
)

@Dao
interface ArticuloDao {
    @Query("SELECT * FROM articulo")
    fun getAll(): List<Articulo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contacto: Articulo): Long

    @Update
    fun update(vararg articulos: Articulo)

    @Delete
    fun delete(articulos: Articulo)

    @Insert
    fun insertMultipleArticulos(articulos: List<Articulo>)

    @Query("SELECT * FROM articulo WHERE nombre LIKE :nombre LIMIT 1")
    fun findByName(nombre: String): Articulo?
}

@Database(entities = [Articulo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articuloDao(): ArticuloDao
}

object DatabaseManager {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val newInstance = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "lista-compras"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            instance = newInstance
            newInstance
        }
    }
}