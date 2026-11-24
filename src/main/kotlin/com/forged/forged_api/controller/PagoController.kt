package com.forged.forged_api.controller

import com.forged.forged_api.entity.Pago
import com.forged.forged_api.repository.PagoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/pagos")
class PagoController(private val repo: PagoRepository) {

    @GetMapping
    fun todos(@RequestParam(required = false) email: String?): List<Pago> =
        if (email.isNullOrBlank()) {
            repo.findAllByOrderByIdAsc()
        } else {
            repo.findAllByEmailUsuarioOrderByIdAsc(email.trim().lowercase())
        }

    @GetMapping("/{id}")
    fun porId(@PathVariable id: Long): ResponseEntity<Pago> =
        repo.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun crear(@RequestBody pago: Pago): ResponseEntity<Pago> {
        pago.id = null
        if (pago.plan.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El plan es obligatorio")
        }
        pago.emailUsuario = pago.emailUsuario?.trim()?.lowercase()
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(pago))
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody datos: Pago): Pago {
        val actual = repo.findById(id).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado")
        actual.plan = datos.plan
        actual.monto = datos.monto
        actual.fechaInicio = datos.fechaInicio
        actual.fechaVencimiento = datos.fechaVencimiento
        actual.metodoPago = datos.metodoPago
        actual.estado = datos.estado
        actual.emailUsuario = datos.emailUsuario?.trim()?.lowercase()
        return repo.save(actual)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        if (!repo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado")
        }
        repo.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
