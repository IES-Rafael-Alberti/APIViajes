package com.es.apiSpringBoot.controller.DTOClasses

import com.es.apiSpringBoot.model.Usuario
import com.es.apiSpringBoot.model.enumclasses.UsuarioRol


data class UsuarioInput(
    val username: String,
    val password: String,
)

fun UsuarioInput.toFull() = Usuario(
    id = null,
    username = this.username,
    password = this.password,
    rol = UsuarioRol.ROLE_USER
)