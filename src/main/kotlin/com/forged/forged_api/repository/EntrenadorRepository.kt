package com.forged.forged_api.repository

import com.forged.forged_api.entity.Entrenador
import org.springframework.data.jpa.repository.JpaRepository

interface EntrenadorRepository : JpaRepository<Entrenador, Long> {
    fun findAllByOrderByIdAsc(): List<Entrenador>
    fun findFirstByOrderByIdAsc(): Entrenador?
}
