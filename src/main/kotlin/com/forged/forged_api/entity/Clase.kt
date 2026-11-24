package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "clases")
class Clase(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var nombre: String = "",
    var dia: String = "",
    var horario: String = "",
    var entrenador: String = "",
    var cuposOcupados: Int = 0,
    var cuposTotal: Int = 0,
    var duracionMin: Int = 0,
    var descripcion: String = ""
)
