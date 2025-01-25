package com.es.apiSpringBoot.repository

import com.es.apiSpringBoot.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {

    fun findByUsername(username: String) : Optional<Usuario>

}