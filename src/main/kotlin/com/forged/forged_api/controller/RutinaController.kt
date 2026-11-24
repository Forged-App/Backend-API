package com.forged.forged_api.controller

import com.forged.forged_api.entity.Rutina
import com.forged.forged_api.repository.RutinaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/rutinas")
class RutinaController(private val repo: RutinaRepository) {

    @GetMapping
    fun todas(): List<Rutina> = repo.findAllByOrderByIdAsc()

    @GetMapping("/{id}")
    fun porId(@PathVariable id: Long): ResponseEntity<Rutina> =
        repo.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun crear(@RequestBody rutina: Rutina): ResponseEntity<Rutina> {
        rutina.id = null
        if (rutina.nombre.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la rutina es obligatorio")
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(rutina))
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody datos: Rutina): Rutina {
        val actual = repo.findById(id).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Rutina no encontrada")
        actual.nombre = datos.nombre
        actual.tipo = datos.tipo
        actual.nivel = datos.nivel
        actual.duracionMin = datos.duracionMin
        actual.numeroEjercicios = datos.numeroEjercicios
        actual.detalle = datos.detalle
        actual.subtitulo = datos.subtitulo
        return repo.save(actual)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        if (!repo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Rutina no encontrada")
        }
        repo.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
