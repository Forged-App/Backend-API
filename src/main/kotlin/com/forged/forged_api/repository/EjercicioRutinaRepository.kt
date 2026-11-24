package com.forged.forged_api.repository

import com.forged.forged_api.entity.EjercicioRutina
import org.springframework.data.jpa.repository.JpaRepository

interface EjercicioRutinaRepository : JpaRepository<EjercicioRutina, Long> {
    fun findAllByOrderByIdAsc(): List<EjercicioRutina>
    fun findByIdRutinaOrderByIdAsc(idRutina: Long): List<EjercicioRutina>
}
