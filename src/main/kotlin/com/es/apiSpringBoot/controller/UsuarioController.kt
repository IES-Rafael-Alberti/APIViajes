package com.es.apiSpringBoot.controller

import com.es.apiSpringBoot.controller.trimmedClasses.UserRegisterLogin
import com.es.apiSpringBoot.model.Usuario
import com.es.apiSpringBoot.model.enumclasses.UsuarioRol
import com.es.apiSpringBoot.service.TokenServiceAPI
import com.es.apiSpringBoot.service.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private lateinit var usuarioService: UsuarioService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var tokenServiceAPI: TokenServiceAPI




    @PostMapping("/register")
    fun register(
        @RequestBody newUsuario: UserRegisterLogin
    ): ResponseEntity<Usuario> {
        val newUsuario = Usuario(null, newUsuario.username, newUsuario.password, null)
        val registeredUsuario = usuarioService.registerUsuario(newUsuario)
        return ResponseEntity(registeredUsuario, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<Usuario>> {
        val usuarios = usuarioService.findAllUsers()
        return ResponseEntity.ok(usuarios)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && (#id == authentication.principal.id || hasRole('ADMIN'))")
    fun getUserById(@PathVariable id: Long): ResponseEntity<Usuario> {
        val usuario = usuarioService.findUserById(id)
        return ResponseEntity.ok(usuario)
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() && (#id == authentication.principal.id || hasRole('ADMIN'))")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody updatedUser: Usuario
    ): ResponseEntity<Usuario> {
        val usuario = usuarioService.updateUser(id, updatedUser)
        return ResponseEntity.ok(usuario)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && (#id == authentication.principal.id || hasRole('ADMIN'))")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        usuarioService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    /*
    MÉTODO (ENDPOINT) PARA HACER UN LOGIN
     */
    @PostMapping("/login")
    fun login(@RequestBody usuario: UserRegisterLogin): ResponseEntity<Any> {
        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(usuario.username, usuario.password)
            )
        } catch (e: AuthenticationException) {
            return ResponseEntity(mapOf("mensaje" to "Credenciales incorrectas"), HttpStatus.UNAUTHORIZED)
        }

        val token = tokenServiceAPI.generarToken(authentication)
        return ResponseEntity(mapOf("mensaje" to "Login exitoso", "token" to token), HttpStatus.OK)
    }

}