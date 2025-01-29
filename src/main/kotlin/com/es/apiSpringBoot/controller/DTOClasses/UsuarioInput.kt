package com.es.apiSpringBoot.controller.DTOClasses

import com.es.apiSpringBoot.model.Usuario


data class UsuarioInput(
    val username: String,
    val password: String,
)

fun UsuarioInput.toFull() = Usuario(
    id = null,
    username = this.username,
    password = this.password,
    rol = null
)