package com.forged.forged_api.controller

import com.forged.forged_api.entity.Disponibilidad
import com.forged.forged_api.repository.DisponibilidadRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/disponibilidad")
class DisponibilidadController(private val repo: DisponibilidadRepository) {

    @GetMapping
    fun listar(
        @RequestParam(required = false) idEntrenador: Long?,
        @RequestParam(required = false) dia: String?
    ): List<Disponibilidad> = when {
        idEntrenador != null && !dia.isNullOrBlank() ->
            repo.findByIdEntrenadorAndDiaOrderByIdAsc(idEntrenador, dia)
        idEntrenador != null ->
            repo.findAllByOrderByIdAsc().filter { it.idEntrenador == idEntrenador }
        !dia.isNullOrBlank() ->
            repo.findAllByOrderByIdAsc().filter { it.dia == dia }
        else -> repo.findAllByOrderByIdAsc()
    }

    @GetMapping("/{id}")
    fun porId(@PathVariable id: Long): ResponseEntity<Disponibilidad> =
        repo.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun crear(@RequestBody disponibilidad: Disponibilidad): ResponseEntity<Disponibilidad> {
        disponibilidad.id = null
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(disponibilidad))
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody datos: Disponibilidad): Disponibilidad {
        val actual = repo.findById(id).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidad no encontrada")
        actual.idEntrenador = datos.idEntrenador
        actual.entrenador = datos.entrenador
        actual.dia = datos.dia
        actual.hora = datos.hora
        actual.disponible = datos.disponible
        return repo.save(actual)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        if (!repo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidad no encontrada")
        }
        repo.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
