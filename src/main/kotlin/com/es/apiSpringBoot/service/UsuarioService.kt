package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.exception.BadRequestException
import com.es.apiSpringBoot.exception.ConflictException
import com.es.apiSpringBoot.exception.NotFoundException
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
        //Comprueba que el nombre sea unico
        if (usuarioRepository.findByUsername(usuario.username.toString()).isPresent) {
            throw ConflictException("Usuario con nombre ${usuario.username} ya existe")
        }

        //Es imposible registrar a un usuario con ADMIN desde la API se tiene que hacer manualmente
        usuario.rol = UsuarioRol.ROLE_USER
        usuario.password = passwordEncoder.encode(usuario.password)

        return usuarioRepository.save(usuario)
    }

    fun findAllUsers(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    fun findUserById(id: Long): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow{
                NotFoundException("No se encontró el destino con id $id")
            }
    }

    fun updateUser(id: Long, nuevoUsuario: Usuario): Usuario {
        //Comprueba la validez de los datos
        validateUsuario(nuevoUsuario)
        //Comprueba que el nombre sea unico
        if (usuarioRepository.findByUsername(nuevoUsuario.username.toString()).isPresent) {
            throw ConflictException("Usuario con nombre ${nuevoUsuario.username} ya existe")
        }
        //Comrpueba que el id de usuario exista y guarda sus datos
        val oldUser = usuarioRepository.findById(id)
            .orElseThrow{ NotFoundException("El usuario que se intenta actualizar no existe") }

        //Mantiene siempre el rol que tenía para evitar fallas de seuguridad
        nuevoUsuario.rol = oldUser?.rol
        return usuarioRepository.save(nuevoUsuario)
    }

    fun deleteUser(id: Long) {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow{ NotFoundException("El usuario que se intenta borrar no existe") }
        usuarioRepository.delete(usuario)
    }

    //Funcion auxiliar para comprobar si los datos del Usuario son correctos
    private fun validateUsuario(usuario: Usuario) {
        val errors = mutableListOf<String>()

        // Comprueba que no haya datos vacios
        if (usuario.username.isNullOrBlank()) {
            errors.add("El nombre del usuario no puede estar vacío")
        }
        if (usuario.password.isNullOrBlank()) {
            errors.add("La contrasena no puede estar vacia")
        }


        //En este caso no recortamos la contrasena porque los espacios son parte de ella
        usuario.username = usuario.username!!.trim()

        // Comprueba la longitud de los datos y que no sobrepasen el limite
        if (usuario.username!!.length > 50) {
            throw IllegalArgumentException("La longitud maxima del nombre es 50 caracteres")
        }

        if (usuario.password!!.length > 50) {
            errors.add("La longitud máxima del nombre es 50 caracteres")
        }

        // Comprueba que la contraseña sigue estandares mínimos
        if (!usuario.password!!.matches(Regex("^(?=.*[A-Za-z])(?=.*\\\\d).{8,}\$"))) {
            errors.add("La longitud máxima del nombre es 50 caracteres")
        }

        if (errors.isNotEmpty()) {
            throw BadRequestException(errors.joinToString(". "))
        }
    }
}
