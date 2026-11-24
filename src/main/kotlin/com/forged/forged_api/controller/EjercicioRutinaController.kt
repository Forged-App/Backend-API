package com.forged.forged_api.controller

import com.forged.forged_api.entity.EjercicioRutina
import com.forged.forged_api.repository.EjercicioRutinaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/ejercicios")
class EjercicioRutinaController(private val repo: EjercicioRutinaRepository) {

    @GetMapping
    fun listar(@RequestParam(required = false) idRutina: Long?): List<EjercicioRutina> =
        if (idRutina == null) repo.findAllByOrderByIdAsc()
        else repo.findByIdRutinaOrderByIdAsc(idRutina)

    @GetMapping("/{id}")
    fun porId(@PathVariable id: Long): ResponseEntity<EjercicioRutina> =
        repo.findById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun crear(@RequestBody ejercicio: EjercicioRutina): ResponseEntity<EjercicioRutina> {
        ejercicio.id = null
        if (ejercicio.nombre.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del ejercicio es obligatorio")
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(ejercicio))
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody datos: EjercicioRutina): EjercicioRutina {
        val actual = repo.findById(id).orElse(null)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Ejercicio no encontrado")
        actual.idRutina = datos.idRutina
        actual.nombre = datos.nombre
        actual.tiempo = datos.tiempo
        actual.completado = datos.completado
        return repo.save(actual)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        if (!repo.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Ejercicio no encontrado")
        }
        repo.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
