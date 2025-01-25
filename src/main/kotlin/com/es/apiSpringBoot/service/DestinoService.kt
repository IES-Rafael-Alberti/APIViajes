package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.model.Destino
import com.es.apiSpringBoot.repository.DestinoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DestinoService {

    @Autowired
    private lateinit var destinoRepository: DestinoRepository

    fun createDestino(destino: Destino): Destino {
        // Comprueba que no exista un destino con el mismo nombre
        if (destinoRepository.findByName(destino.name).isPresent) {
            throw RuntimeException("Destination with name ${destino.name} already exists")
        }
        return destinoRepository.save(destino)
    }

    fun findAllDestinos(): List<Destino> {
        return destinoRepository.findAll()
    }

    fun findDestinoById(id: Long): Destino {
        return destinoRepository.findById(id)
            .orElseThrow { RuntimeException("Destination not found") }
    }

    fun updateDestino(id: Long, newDestino: Destino): Destino {
        return destinoRepository.save(newDestino)
    }

    fun deleteDestino(id: Long) {
        val destino = findDestinoById(id)
        destinoRepository.delete(destino)
    }
}