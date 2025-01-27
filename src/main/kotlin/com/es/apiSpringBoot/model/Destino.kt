package com.es.apiSpringBoot.model

import jakarta.persistence.*

@Entity
@Table(name = "destinos")
data class Destino(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(unique = true, nullable = false,length = 50)
    var name: String?,

    @Column(nullable = false, length = 50)
    var country: String?
) {
    constructor() : this(
        0,
        "",
        ""
    )
}