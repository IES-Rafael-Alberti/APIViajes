package com.es.apiSpringBoot.model

import com.es.apiSpringBoot.model.enumclasses.UsuarioRol
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull


@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(unique = true, nullable = false)
    var username: String?,

    @Column(nullable = false)
    var password: String?,

    @Enumerated(EnumType.STRING)
    var rol: UsuarioRol?
) {
    // Constructor sin argumentos para JPA, no se usa es solo para que no de fallos
    constructor() : this(
        0,
        "",
        "",
        UsuarioRol.ROLE_USER
    )
}