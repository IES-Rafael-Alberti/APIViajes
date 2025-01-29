package com.es.apiSpringBoot.controller.DTOClasses

import com.es.apiSpringBoot.model.Destino

data class DestinoInput(
    val name: String,
    val country: String,
)

fun DestinoInput.toFull() = Destino(
    id = null,
    name = this.name,
    country = this.country,
)