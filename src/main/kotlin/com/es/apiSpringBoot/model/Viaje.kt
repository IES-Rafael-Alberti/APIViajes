package com.es.apiSpringBoot.model

import com.es.apiSpringBoot.model.enumclasses.MethodOfTravel
import jakarta.persistence.*
import java.util.Date


@Entity
@Table(name = "viajes")
data class Viaje(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    var destination: Destino?,

    @Column(nullable = false)
    var date: Date?,

    @Enumerated(EnumType.STRING)
    var methodOfTravel: MethodOfTravel?,

    @ManyToMany(targetEntity = Usuario::class)
    @JoinTable(
        name = "viaje_usuario",
        joinColumns = [JoinColumn(name = "viaje_id")],
        inverseJoinColumns = [JoinColumn(name = "usuario_id")]
    )
    var participants: Set<Usuario?>?
) {
    // Constructor sin argumentos para JPA, no se usa es solo para que no de fallos
    constructor() : this(
        0,
        Destino(),
        Date(),
        MethodOfTravel.CAR,
        setOf()
    )
}