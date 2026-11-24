package com.forged.forged_api.repository

import com.forged.forged_api.entity.Disponibilidad
import org.springframework.data.jpa.repository.JpaRepository

interface DisponibilidadRepository : JpaRepository<Disponibilidad, Long> {
    fun findAllByOrderByIdAsc(): List<Disponibilidad>
    fun findByIdEntrenadorAndDiaOrderByIdAsc(idEntrenador: Long, dia: String): List<Disponibilidad>
}
