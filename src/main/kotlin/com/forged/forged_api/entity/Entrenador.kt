package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "entrenadores")
class Entrenador(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var nombre: String = "",
    var rol: String = "",
    var calificacion: Double = 0.0,
    var resenas: Int = 0,
    var especialidades: String = "",
    var sobreMi: String = ""
)
