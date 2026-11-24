package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "reservas")
class Reserva(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var idClase: Long? = null,
    var titulo: String = "",
    var fecha: String = "",
    var hora: String = "",
    var entrenador: String = "",
    var duracion: String = "",
    var estado: String = "Activa",
    var esGrupal: Boolean = true,
    var emailUsuario: String? = null
)
