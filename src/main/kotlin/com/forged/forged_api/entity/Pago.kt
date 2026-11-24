package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "pagos")
class Pago(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var plan: String = "",
    var monto: String = "",
    var fechaInicio: String = "",
    var fechaVencimiento: String = "",
    var metodoPago: String = "",
    var estado: String = "",
    var emailUsuario: String? = null
)
