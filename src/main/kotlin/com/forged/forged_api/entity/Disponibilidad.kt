package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "disponibilidad")
class Disponibilidad(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var idEntrenador: Long = 0,
    var entrenador: String = "",
    var dia: String = "",
    var hora: String = "",
    var disponible: Boolean = true
)
