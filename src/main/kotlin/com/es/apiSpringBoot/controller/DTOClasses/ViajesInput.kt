package com.es.apiSpringBoot.controller.DTOClasses

import com.es.apiSpringBoot.model.Viaje
import com.es.apiSpringBoot.model.enumclasses.MethodOfTravel
import java.util.Date

data class ViajesInput(
    val idDestination: Long?,
    val date: Date?,
    val methodOfTravel: MethodOfTravel?,
    val participants: Array<Long?>?,
)

fun ViajesInput.toFull() = Viaje(
    id = null,
    destination = null,
    date = this.date,
    methodOfTravel = this.methodOfTravel,
    participants = null
)