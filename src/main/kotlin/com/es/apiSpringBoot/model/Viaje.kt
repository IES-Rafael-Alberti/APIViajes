package com.es.apiSpringBoot.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "viajes")
data class Viaje(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    var destination: Destino,

    @Column(nullable = false)
    var date: Date,

    @Column(nullable = false)
    var methodOfTravel: String, //"CAR,BUS,TRAIN,PLANE,BOAT"

    @ManyToMany(targetEntity = Usuario::class)
    @JoinTable(
        name = "viaje_usuario",
        joinColumns = [JoinColumn(name = "viaje_id")],
        inverseJoinColumns = [JoinColumn(name = "usuario_id")]
    )
    var participants: Set<Usuario>
) {
    constructor() : this(
        null,
        Destino(),
        Date(),
        "",
        setOf()
    )
}