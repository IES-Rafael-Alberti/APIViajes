package com.es.apiSpringBoot.controller.DTOClasses

import com.es.apiSpringBoot.model.Viaje
import com.es.apiSpringBoot.model.enumclasses.MethodOfTravel
import java.text.SimpleDateFormat

data class ViajeResponse(
    val id: Long?,
    val destinationName: String?,
    val date: String?,  // Changed from Date to String
    val methodOfTravel: MethodOfTravel?,
    val participantNames: Set<String>?
)

// Funcion para transformar viaje es su clase para las respuestas
fun Viaje.toResponse() = ViajeResponse(
    id = this.id,
    destinationName = this.destination?.name,
    date = this.date?.let { SimpleDateFormat("yyyy-MM-dd").format(it) },
    methodOfTravel = this.methodOfTravel,
    participantNames = this.participants?.mapNotNull { it?.username }?.toSet()
)