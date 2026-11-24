package com.forged.forged_api.repository

import com.forged.forged_api.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, String>
