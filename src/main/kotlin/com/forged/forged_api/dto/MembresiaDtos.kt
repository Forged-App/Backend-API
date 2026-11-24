package com.forged.forged_api.dto

data class MembresiaRequest(
    val tipoPlan: String = "",
    val duracion: String = "",
    val costo: String = "",
    val fechaInicio: String = "",
    val fechaVencimiento: String = "",
    val metodoPago: String = "",
    val emailUsuario: String? = null
)
