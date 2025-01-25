package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.model.Usuario
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

    /*
    TODO
     */
    override fun loadUserByUsername(username: String?): UserDetails {
        var usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow()

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.rol)
            .build()
    }


    /*
    MÉTODO PARA INSERTAR UN USUARIO
     */
    fun registerUsuario(user: Usuario): Usuario {
        if (usuarioRepository.findByUsername(user.username).isPresent) {
            throw RuntimeException("El nombre de usuario ya está en uso")
        }

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
        return usuarioRepository.save(nuevoUsuario)
    }

    fun deleteUser(id: Long) {
        val user = findUserById(id)
        usuarioRepository.delete(user)
    }
}
