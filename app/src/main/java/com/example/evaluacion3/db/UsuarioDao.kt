package com.example.evaluacion3.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface UsuarioDao {
    @Query("SELECT count(*) FROM usuario")
    suspend fun contar(): Int

    @Query("SELECT * FROM usuario ORDER BY fecha ASC")
    suspend fun obtenerSolcitudes(): List<Usuario>

    @Query("SELECT * FROM usuario WHERE id = :id")
    suspend fun encontrarPorId(id:Int): Usuario

    @Insert
    suspend fun insertar(usuario: Usuario): Long

    @Delete
    suspend fun borrar(usuario: Usuario)

    @Update
    suspend fun actualizar(vararg usuario: Usuario)

}
