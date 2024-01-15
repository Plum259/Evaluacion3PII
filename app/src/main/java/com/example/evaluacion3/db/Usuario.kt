package com.example.evaluacion3.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity
data class Usuario (
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val nombre:String,
    val rut:Int,
    val email:String,
    val telefono:Int,
    val latitud:Double,
    val longitud:Double,
    val imagenFrente:String?,
    val imagenTrasera:String?,
    val fecha:String
)
