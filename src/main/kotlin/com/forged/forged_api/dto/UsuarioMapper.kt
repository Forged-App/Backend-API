package com.forged.forged_api.dto

import com.forged.forged_api.entity.Usuario

fun Usuario.aResponse() = UsuarioResponse(email = email, nombre = nombre)
