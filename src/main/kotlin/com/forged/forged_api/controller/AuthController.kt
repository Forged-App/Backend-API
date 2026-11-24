package com.forged.forged_api.controller

import com.forged.forged_api.dto.LoginRequest
import com.forged.forged_api.dto.RecuperarResponse
import com.forged.forged_api.dto.RegistroRequest
import com.forged.forged_api.dto.UsuarioResponse
import com.forged.forged_api.dto.aResponse
import com.forged.forged_api.entity.Usuario
import com.forged.forged_api.repository.UsuarioRepository
import com.forged.forged_api.security.PasswordHasher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class AuthController(private val usuarios: UsuarioRepository) {

    @PostMapping("/registro")
    fun registrar(@RequestBody request: RegistroRequest): ResponseEntity<UsuarioResponse> {
        val email = request.email.trim().lowercase()
        if (request.nombre.isBlank() || email.isBlank() || request.password.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Completa todos los campos")
        }
        if (usuarios.existsById(email)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado")
        }
        val usuario = usuarios.save(
            Usuario(
                email = email,
                nombre = request.nombre.trim(),
                password = PasswordHasher.hash(email, request.password)
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario.aResponse())
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): UsuarioResponse {
        val email = request.email.trim().lowercase()
        val usuario = usuarios.findById(email).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas")

        if (usuario.password != PasswordHasher.hash(email, request.password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas")
        }
        return usuario.aResponse()
    }

    @GetMapping("/usuario")
    fun usuario(@RequestParam email: String): UsuarioResponse {
        val usuario = usuarios.findById(email.trim().lowercase()).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")
        return usuario.aResponse()
    }

    @GetMapping("/recuperar")
    fun recuperar(@RequestParam email: String): RecuperarResponse {
        val existe = usuarios.existsById(email.trim().lowercase())
        return RecuperarResponse(
            existe = existe,
            mensaje = if (existe) {
                "El correo está registrado. Puedes restablecer tu contraseña."
            } else {
                "No existe una cuenta con ese correo."
            }
        )
    }
}
