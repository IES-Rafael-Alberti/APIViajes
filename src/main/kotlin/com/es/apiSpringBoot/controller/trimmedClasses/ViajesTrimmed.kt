package com.es.apiSpringBoot.controller.trimmedClasses

import com.es.apiSpringBoot.model.enumclasses.MethodOfTravel
import java.util.Date

data class ViajesTrimmed(
    val idDestination: String?,
    val date: Date?,
    val methodOfTravel: MethodOfTravel?,
)