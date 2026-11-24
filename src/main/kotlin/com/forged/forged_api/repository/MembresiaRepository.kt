package com.forged.forged_api.repository

import com.forged.forged_api.entity.Membresia
import org.springframework.data.jpa.repository.JpaRepository

interface MembresiaRepository : JpaRepository<Membresia, Long> {
    fun findAllByOrderByIdDesc(): List<Membresia>
    fun findFirstByOrderByIdDesc(): Membresia?
    fun findFirstByEmailUsuarioOrderByIdDesc(email: String): Membresia?
}
