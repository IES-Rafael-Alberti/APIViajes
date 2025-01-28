package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.exception.BadRequestException
import com.es.apiSpringBoot.exception.ConflictException
import com.es.apiSpringBoot.exception.NotFoundException
import com.es.apiSpringBoot.model.Destino
import com.es.apiSpringBoot.repository.DestinoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DestinoService {

    @Autowired
    private lateinit var destinoRepository: DestinoRepository

    fun createDestino(destino: Destino): Destino {
        //Comprueba la validez de los datos
        validateDestino(destino)

        //Nullifica el id para evitar errores, el id se pone automatico de todas formas
        destino.id = null

        // Comprueba que no exista un destino con el mismo nombre
        if (destinoRepository.findByName(destino.name.toString()).isPresent) {
            throw ConflictException("Destination con nombre ${destino.name} ya existe")
        }
        return destinoRepository.save(destino)
    }

    fun findAllDestinos(): List<Destino> {
        return destinoRepository.findAll()
    }

    fun findDestinoById(id: Long): Destino {
        return destinoRepository.findById(id)
            .orElseThrow{
                NotFoundException("No se encontró el destino con id $id")
            }
    }

    fun updateDestino(id: Long, newDestino: Destino): Destino {
        //Comprueba la validez de los datos
        validateDestino(newDestino)
        // Comprueba que no exista un destino con el mismo nombre
        if (destinoRepository.findByName(newDestino.name.toString()).isPresent) {
            throw ConflictException("Destination con nombre ${newDestino.name} ya existe")
        }
        //Comprueba que el destino que se esta intentado cambiar exsite
        val oldDestino = destinoRepository.findById(id)
            .orElseThrow{ NotFoundException("El destino que se intenta actualizar no existe") }

        return destinoRepository.save(newDestino)
    }

    fun deleteDestino(id: Long) {
        val destino = destinoRepository.findById(id)
            .orElseThrow{ NotFoundException("El destino que se intenta borrar no existe") }
        destinoRepository.delete(destino)
    }


    //Funcion auxiliar para comprobar si los datos del Destino son correctos
    private fun validateDestino(destino: Destino) {
        val errors = mutableListOf<String>()

        if (destino.name.isNullOrBlank()) {
            errors.add("El nombre del destino no puede estar vacío")
        }else{
            destino.name = destino.name?.trim()
            if ((destino.name?.length ?: 0) > 50) {
                errors.add("La longitud máxima del nombre es 50 caracteres")
            }
        }
        if (destino.country.isNullOrBlank()) {
            errors.add("El nombre del país no puede estar vacío")

        }else{
            destino.country = destino.country?.trim()
            if ((destino.country?.length ?: 0) > 50) {
                errors.add("La longitud máxima del nombre del país es 50 caracteres")
            }
        }

        if (errors.isNotEmpty()) {
            throw BadRequestException(errors.joinToString(". "))
        }
    }
    }


