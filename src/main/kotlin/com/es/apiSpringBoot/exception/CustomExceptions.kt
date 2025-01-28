package com.es.apiSpringBoot.exception

import org.springframework.http.HttpStatus


open class ApiException(message: String, val status: HttpStatus) : RuntimeException(message)

//Se usa cuando algo que se está buscando no existe
class NotFoundException(message: String) :
    ApiException(message, HttpStatus.NOT_FOUND)

//Se usa cuando un id o name ya está en uso
class ConflictException(message: String) :
    ApiException(message, HttpStatus.CONFLICT)

//Se usa cuando algunos datos que se han pasado no siguen las regulaciones
class BadRequestException(message: String) :
    ApiException(message, HttpStatus.BAD_REQUEST)