package com.forged.forged_api.dto

data class ReservaRequest(
    val idClase: Long? = null,
    val titulo: String = "",
    val fecha: String = "",
    val hora: String = "",
    val entrenador: String = "",
    val duracion: String = "",
    val esGrupal: Boolean = true,
    val emailUsuario: String? = null
)
