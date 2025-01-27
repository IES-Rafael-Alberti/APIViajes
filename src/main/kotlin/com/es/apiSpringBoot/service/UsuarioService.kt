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
import java.util.Optional


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


    fun registerUsuario(usuario: Usuario): Usuario {

        //Comprueba la validez de los datos
        validateUsuario(usuario)
        usuarioRepository.findByUsername(usuario.username.toString())
        //Comprueba que el nombre sea unico
        usernameUnique(usuario.username.toString())

        //Es imposible registrar a un usuario con ADMIN desde la API se tiene que hacer manualmente
        usuario.rol = UsuarioRol.ROLE_USER
        usuario.password = passwordEncoder.encode(usuario.password)

        return usuarioRepository.save(usuario)
    }

    fun findAllUsers(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    fun findUserById(id: Long): Optional<Usuario?> {
        return usuarioRepository.findById(id)
    }

    fun updateUser(id: Long, nuevoUsuario: Usuario): Usuario {
        //Comprueba la validez de los datos
        validateUsuario(nuevoUsuario)
        //Comprueba que el nombre sea unico
        usernameUnique(nuevoUsuario.username.toString())
        //Comrpueba que el id de usuario exista y guarda sus datos
        val oldUser = findUserById(id)
            .orElseThrow { RuntimeException("El usuario que se intenta modifcar no existe") }

        //Mantiene siempre el rol que tenía para evitar fallas de seuguridad
        nuevoUsuario.rol = oldUser?.rol
        return usuarioRepository.save(nuevoUsuario)
    }

    fun deleteUser(id: Long) {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { RuntimeException("El usuario que se intenta borrar no existe") }
        usuarioRepository.delete(usuario)
    }

    //Comprueba que el nombre de usuario sea unico
    private fun usernameUnique(username: String){
        val userExists = usuarioRepository.findByUsername(username)
        if (userExists.isPresent) {
            throw { RuntimeException("Ese nombre de usuario ya esta en uso")} as Throwable
        }

    }

    //Funcion auxiliar para comprobar si los datos del Usuario son correctos
    private fun validateUsuario(usuario: Usuario) {
        // Comprueba que no haya datos vacios
        if (usuario.username.isNullOrBlank()) {
            throw IllegalArgumentException("El nombre del destino no puede estar vacio")
        }
        if (usuario.password.isNullOrBlank()) {
            throw IllegalArgumentException("El nombre del país no puede estar vacio")
        }


        //En este caso no recortamos la contrasena porque los espacios son parte de ella
        usuario.username = usuario.username!!.trim()

        // Comprueba la longitud de los datos y que no sobrepasen el limite
        if (usuario.username!!.length > 50) {
            throw IllegalArgumentException("La longitud maxima del nombre es 50 caracteres")
        }

        if (usuario.password!!.length > 50) {
            throw IllegalArgumentException("La longitud maxima de la contrasena son 50 caracteres")
        }

        // Check if country contains only valid characters (optional)
        if (!usuario.password!!.matches(Regex("^(?=.*[A-Za-z])(?=.*\\\\d).{8,}\$"))) {
            throw IllegalArgumentException("La contrasena debe ser 8 caracteres o mas y tener al menos una letra y un numero")
        }
    }
}
