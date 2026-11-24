package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "membresias")
class Membresia(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var tipoPlan: String = "",
    var duracion: String = "",
    var costo: String = "",
    var fechaInicio: String = "",
    var fechaVencimiento: String = "",
    var metodoPago: String = "",
    var emailUsuario: String? = null
)
