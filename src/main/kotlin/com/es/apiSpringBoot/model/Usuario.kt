package com.es.apiSpringBoot.model

import jakarta.persistence.*

@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var username: String,

    @Column(nullable = false)
    var password: String,

    var roles: String? = "ROLE_USER" // "ROLE_USER,ROLE_ADMIN" , ROLE_USER por defecto
) {
    // Constructor sin argumentos para JPA, no se usa es solo para que no de fallos
    constructor() : this(
        null,
        "",
        "",
        "ROLE_USER"
    )
}