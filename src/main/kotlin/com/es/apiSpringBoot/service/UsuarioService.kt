package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.model.Usuario
import com.es.apiSpringBoot.model.enumclasses.UsuarioRol
import com.es.apiSpringBoot.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun loadUserByUsername(username: String?): UserDetails {
        var usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow()

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.rol.toString())
            .build()
    }


    fun registerUsuario(user: Usuario): Usuario {

        //Es imposible registrar a un usuario con ADMIN se tiene que hacer manualmente
        user.rol = UsuarioRol.ROLE_USER
        user.password = passwordEncoder.encode(user.password)

        return usuarioRepository.save(user)
    }

    fun findAllUsers(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    fun findUserById(id: Long): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow { RuntimeException("User not found") }
    }

    fun updateUser(id: Long, nuevoUsuario: Usuario): Usuario {
        val oldUser = findUserById(id)

        //Mantiene siempre el rol que tenía para evitar fallas de seuguridad
        nuevoUsuario.rol = oldUser.rol
        return usuarioRepository.save(nuevoUsuario)
    }

    fun deleteUser(id: Long) {
        val user = findUserById(id)
        usuarioRepository.delete(user)
    }
}
