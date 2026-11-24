package com.forged.forged_api.repository

import com.forged.forged_api.entity.Reserva
import org.springframework.data.jpa.repository.JpaRepository

interface ReservaRepository : JpaRepository<Reserva, Long> {
    fun findAllByOrderByIdDesc(): List<Reserva>
    fun findByEmailUsuarioOrderByIdDesc(email: String): List<Reserva>
}
