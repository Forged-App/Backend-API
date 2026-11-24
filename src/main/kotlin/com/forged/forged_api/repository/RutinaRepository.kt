package com.forged.forged_api.repository

import com.forged.forged_api.entity.Rutina
import org.springframework.data.jpa.repository.JpaRepository

interface RutinaRepository : JpaRepository<Rutina, Long> {
    fun findAllByOrderByIdAsc(): List<Rutina>
}
