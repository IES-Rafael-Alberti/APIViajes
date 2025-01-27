package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.model.Destino
import com.es.apiSpringBoot.repository.DestinoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class DestinoService {

    @Autowired
    private lateinit var destinoRepository: DestinoRepository

    fun createDestino(destino: Destino): Destino {
        //Comprueba la validez de los datos
        validateDestino(destino)
        // Comprueba que no exista un destino con el mismo nombre
        if (destinoRepository.findByName(destino.name.toString()).isPresent) {
            throw RuntimeException("Destination con nombre ${destino.name} ya existe")
        }
        return destinoRepository.save(destino)
    }

    fun findAllDestinos(): List<Destino> {
        return destinoRepository.findAll()
    }

    fun findDestinoById(id: Long): Optional<Destino> {
        return destinoRepository.findById(id)
    }

    fun updateDestino(id: Long, newDestino: Destino): Destino {
        destinoRepository.findById(id)
            .orElseThrow{ RuntimeException("El usuario que se intenta actualizar no existe") }
        return destinoRepository.save(newDestino)
    }

    fun deleteDestino(id: Long) {
        val destino = destinoRepository.findById(id)
            .orElseThrow{ RuntimeException("El usuario que se intenta borrar no existe") }
        destinoRepository.delete(destino)
    }


    //Funcion auxiliar para comprobar si los datos del Destino son correctos
    private fun validateDestino(destino: Destino) {
        // Comprueba que no haya datos vacios
        if (destino.name.isNullOrBlank()) {
            throw IllegalArgumentException("El nombre del destino no puede estar vacio")
        }
        if (destino.country.isNullOrBlank()) {
            throw IllegalArgumentException("El nombre del país no puede estar vacio")
        }

        destino.name = destino.name!!.trim()
        destino.country = destino.country!!.trim()

        // Comprueba la longitud de los datos y que no sobrepasen el limite
        if (destino.name!!.length > 50) {
            throw IllegalArgumentException("La longitud maxima del nombre es 50 caracteres")
        }

        if (destino.country!!.length > 50) {
            throw IllegalArgumentException("La longitud maxima del nombre del pais es 50 caracteres")
        }
    }


}