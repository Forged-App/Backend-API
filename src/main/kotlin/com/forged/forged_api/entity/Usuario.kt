package com.forged.forged_api.entity

import jakarta.persistence.*

@Entity
@Table(name = "usuarios")
class Usuario(
    @Id
    var email: String = "",
    var nombre: String = "",
    var password: String = ""
)
