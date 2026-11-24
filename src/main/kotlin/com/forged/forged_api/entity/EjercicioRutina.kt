package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "ejercicios_rutina")
class EjercicioRutina(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var idRutina: Long = 0,
    var nombre: String = "",
    var tiempo: String = "",
    var completado: Boolean = false
)
