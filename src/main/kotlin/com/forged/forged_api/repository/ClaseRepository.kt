package com.forged.forged_api.repository

import com.forged.forged_api.entity.Clase
import org.springframework.data.jpa.repository.JpaRepository

interface ClaseRepository : JpaRepository<Clase, Long> {
    fun findAllByOrderByIdAsc(): List<Clase>
}
