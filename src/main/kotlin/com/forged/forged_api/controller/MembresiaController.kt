package com.forged.forged_api.controller

import com.forged.forged_api.dto.MembresiaRequest
import com.forged.forged_api.entity.Membresia
import com.forged.forged_api.repository.MembresiaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/membresias")
class MembresiaController(private val repo: MembresiaRepository) {

    @GetMapping
    fun listar(@RequestParam(required = false) email: String?): List<Membresia> =
        if (email.isNullOrBlank()) {
            repo.findAllByOrderByIdDesc()
        } else {
            repo.findAllByOrderByIdDesc()
                .filter { it.emailUsuario == email.trim().lowercase() }
        }

    @GetMapping("/actual")
    fun actual(@RequestParam(required = false) email: String?): ResponseEntity<Membresia> {
        // Si se pide la membresía de un usuario, no se debe devolver la de otro.
        val membresia = if (email.isNullOrBlank()) {
            repo.findFirstByOrderByIdDesc()
        } else {
            repo.findFirstByEmailUsuarioOrderByIdDesc(email.trim().lowercase())
        }

        return membresia
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }

    @PostMapping
    fun crear(@RequestBody request: MembresiaRequest): ResponseEntity<Membresia> {
        if (request.tipoPlan.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de plan es obligatorio")
        }
        val membresia = repo.save(
            Membresia(
                tipoPlan = request.tipoPlan,
                duracion = request.duracion,
                costo = request.costo,
                fechaInicio = request.fechaInicio,
                fechaVencimiento = request.fechaVencimiento,
                metodoPago = request.metodoPago,
                emailUsuario = request.emailUsuario?.trim()?.lowercase()
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(membresia)
    }
}
