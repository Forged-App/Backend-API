package com.forged.forged_api.controller

import com.forged.forged_api.entity.Entrenador
import com.forged.forged_api.repository.EntrenadorRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/entrenadores")
class EntrenadorController(private val repo: EntrenadorRepository) {

    @GetMapping
    fun todos(): List<Entrenador> = repo.findAllByOrderByIdAsc()

    @GetMapping("/primero")
    fun primero(): ResponseEntity<Entrenador> =
        repo.findFirstByOrderByIdAsc()
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()

    @GetMapping("/{id}")
    fun porId(@PathVariable id: Long): ResponseEntity<Entrenador> =
        repo.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun crear(@RequestBody entrenador: Entrenador): ResponseEntity<Entrenador> {
        entrenador.id = null
        if (entrenador.nombre.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio")
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(entrenador))
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody datos: Entrenador): Entrenador {
        val actual = repo.findById(id).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Entrenador no encontrado")
        actual.nombre = datos.nombre
        actual.rol = datos.rol
        actual.calificacion = datos.calificacion
        actual.resenas = datos.resenas
        actual.especialidades = datos.especialidades
        actual.sobreMi = datos.sobreMi
        return repo.save(actual)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        if (!repo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Entrenador no encontrado")
        }
        repo.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
