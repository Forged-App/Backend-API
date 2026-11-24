package com.forged.forged_api.security

import java.security.MessageDigest

/**
 * Hash sencillo con SHA-256 y el correo como sal.
 * Evita guardar contraseñas en texto plano sin agregar dependencias externas.
 */
object PasswordHasher {

    fun hash(email: String, password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest("${email.trim().lowercase()}:$password".toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it.toInt() and 0xff) }
    }
}
