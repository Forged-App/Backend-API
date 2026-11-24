package com.forged.forged_api.controller

import com.forged.forged_api.entity.Clase
import com.forged.forged_api.repository.ClaseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/clases")
class ClaseController(private val repo: ClaseRepository) {

    @GetMapping
    fun todas(): List<Clase> = repo.findAllByOrderByIdAsc()

    @GetMapping("/{id}")
    fun porId(@PathVariable id: Long): ResponseEntity<Clase> =
        repo.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun crear(@RequestBody clase: Clase): ResponseEntity<Clase> {
        clase.id = null
        if (clase.nombre.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la clase es obligatorio")
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(clase))
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody datos: Clase): Clase {
        val actual = repo.findById(id).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada")
        actual.nombre = datos.nombre
        actual.dia = datos.dia
        actual.horario = datos.horario
        actual.entrenador = datos.entrenador
        actual.cuposOcupados = datos.cuposOcupados.coerceAtMost(datos.cuposTotal)
        actual.cuposTotal = datos.cuposTotal
        actual.duracionMin = datos.duracionMin
        actual.descripcion = datos.descripcion
        return repo.save(actual)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        if (!repo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada")
        }
        repo.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
