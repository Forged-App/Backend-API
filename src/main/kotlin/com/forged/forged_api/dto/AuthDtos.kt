package com.forged.forged_api.dto

data class LoginRequest(
    val email: String = "",
    val password: String = ""
)

data class RegistroRequest(
    val nombre: String = "",
    val email: String = "",
    val password: String = ""
)

data class UsuarioResponse(
    val email: String,
    val nombre: String
)

data class RecuperarResponse(
    val existe: Boolean,
    val mensaje: String
)
