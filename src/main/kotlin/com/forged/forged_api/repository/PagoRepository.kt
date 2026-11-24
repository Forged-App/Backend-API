package com.forged.forged_api.repository

import com.forged.forged_api.entity.Pago
import org.springframework.data.jpa.repository.JpaRepository

interface PagoRepository : JpaRepository<Pago, Long> {
    fun findAllByOrderByIdAsc(): List<Pago>
    fun findAllByEmailUsuarioOrderByIdAsc(email: String): List<Pago>
}
