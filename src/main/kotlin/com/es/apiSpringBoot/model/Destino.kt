package com.es.apiSpringBoot.model

import jakarta.persistence.*

@Entity
@Table(name = "destinos")
data class Destino(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var name: String,

    @Column(nullable = false)
    var country: String
) {
    constructor() : this(
        null,
        "",
        ""
    )
}