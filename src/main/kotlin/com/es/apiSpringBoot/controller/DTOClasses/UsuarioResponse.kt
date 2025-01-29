package com.es.apiSpringBoot.controller.DTOClasses

import com.es.apiSpringBoot.model.Usuario
import com.es.apiSpringBoot.model.enumclasses.UsuarioRol

data class UsuarioResponse(
    val id: Long?,
    val username: String?,
    val rol: UsuarioRol?
)

// Transforma la clase usuario para quitarle la contrasena
fun Usuario.toResponse() = UsuarioResponse(
    id = this.id,
    username = this.username,
    rol = this.rol
)
