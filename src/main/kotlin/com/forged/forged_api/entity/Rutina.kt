package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "rutinas")
class Rutina(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var nombre: String = "",
    var tipo: String = "",
    var nivel: String = "",
    var duracionMin: Int = 0,
    var numeroEjercicios: Int = 0,
    var detalle: String = "",
    var subtitulo: String = ""
)
