package com.example.evaluacion3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class], version = 1)
abstract class BaseDatos : RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao

    companion object{
        @Volatile
        private var BASE_DATOS: BaseDatos? = null
        const val BD_NOMBRE = "Solicitudes.bd"

        fun getInstance(contexto:Context) : BaseDatos{
            return BASE_DATOS ?: synchronized(this) {
                Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatos::class.java,
                    BD_NOMBRE
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }
    }
}
