package com.forged.forged_api.controller

import com.forged.forged_api.dto.ReservaRequest
import com.forged.forged_api.entity.Reserva
import com.forged.forged_api.repository.ClaseRepository
import com.forged.forged_api.repository.ReservaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/reservas")
class ReservaController(
    private val reservas: ReservaRepository,
    private val clases: ClaseRepository
) {

    @GetMapping
    fun listar(@RequestParam(required = false) email: String?): List<Reserva> =
        if (email.isNullOrBlank()) {
            reservas.findAllByOrderByIdDesc()
        } else {
            reservas.findByEmailUsuarioOrderByIdDesc(email.trim().lowercase())
        }

    @GetMapping("/{id}")
    fun porId(@PathVariable id: Long): ResponseEntity<Reserva> =
        reservas.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    @Transactional
    fun crear(@RequestBody request: ReservaRequest): ResponseEntity<Reserva> {
        val clase = request.idClase?.let {
            clases.findById(it).orElse(null)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada")
        }

        if (clase != null && clase.cuposOcupados >= clase.cuposTotal) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "No hay cupos disponibles para esta clase")
        }
        if (request.fecha.isBlank() || request.hora.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha y la hora son obligatorias")
        }

        clase?.let {
            it.cuposOcupados += 1
            clases.save(it)
        }

        val reserva = reservas.save(
            Reserva(
                idClase = clase?.id,
                titulo = request.titulo.ifBlank { clase?.nombre ?: "Reserva" },
                fecha = request.fecha,
                hora = request.hora,
                entrenador = request.entrenador.ifBlank { clase?.entrenador ?: "" },
                duracion = request.duracion.ifBlank {
                    clase?.let { "${it.duracionMin} min" } ?: "60 min"
                },
                estado = "Activa",
                esGrupal = request.esGrupal,
                emailUsuario = request.emailUsuario?.trim()?.lowercase()
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva)
    }

    @DeleteMapping("/{id}")
    @Transactional
    fun cancelar(@PathVariable id: Long): ResponseEntity<Void> {
        val reserva = reservas.findById(id).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada")

        reserva.idClase?.let { claseId ->
            clases.findById(claseId).ifPresent { clase ->
                clase.cuposOcupados = maxOf(0, clase.cuposOcupados - 1)
                clases.save(clase)
            }
        }
        reservas.delete(reserva)
        return ResponseEntity.noContent().build()
    }
}
