package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.model.Viaje
import com.es.apiSpringBoot.model.Usuario
import com.es.apiSpringBoot.repository.ViajeRepository
import com.es.apiSpringBoot.repository.DestinoRepository
import com.es.apiSpringBoot.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Date

@Service
class ViajeService {

    @Autowired
    private lateinit var viajeRepository: ViajeRepository

    @Autowired
    private lateinit var destinoRepository: DestinoRepository

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    fun createViaje(viaje: Viaje): Viaje {
        // Comprueba que el destino exista
        destinoRepository.findById(viaje.destination.id).orElseThrow {
            RuntimeException("El destino no existe")
        }

        // Comprueba que los participantes existan
        val validParticipants = viaje.participants.map { participant ->
            usuarioRepository.findById(participant.id)
                .orElseThrow { RuntimeException("Usuario con ID ${participant.id} no existe") }
        }.toSet()

            viaje.participants = validParticipants
            return viajeRepository.save(viaje)

    }


        fun findAllViajes(): List<Viaje> = viajeRepository.findAll()

        fun findViajeById(id: Long): Viaje =
            viajeRepository.findById(id).orElseThrow { RuntimeException("Viaje not found") }

        fun updateViaje(id: Long, updatedViaje: Viaje): Viaje {
            val existingViaje = findViajeById(id)

            destinoRepository.findById(updatedViaje.destination.id).orElseThrow {
                RuntimeException("El destino no existe")
            }

            //Los participantes se tiene que cambiar por separado para evitar fallas de seguridad
            updatedViaje.participants = existingViaje.participants

            return viajeRepository.save(updatedViaje)
        }

        fun deleteViaje(id: Long) {
            val viaje = findViajeById(id)
            viajeRepository.delete(viaje)
        }

        fun findViajesByParticipant(usuarioId: Long): List<Viaje> {
            val usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow { RuntimeException("Este usuario no existe") }

            return viajeRepository.findAll()
                .filter { it.participants.contains(usuario) }
        }

        fun isUserParticipant(viajeId: Long, usuarioId: Long): Boolean {
            val viaje = findViajeById(viajeId)
            return viaje.participants.any { it.id == usuarioId }
        }

    fun addParticipant(viajeId: Long, username: String): Viaje {
        val viaje = findViajeById(viajeId)
        val usuario = usuarioRepository.findByUsername(username)
            .orElseThrow { RuntimeException("User not found") }

        if (viaje.participants.any { it.id == usuario.id }) {
            throw RuntimeException("User is already a participant")
        }

        val updatedParticipants = viaje.participants.toMutableSet()
        updatedParticipants.add(usuario)
        viaje.participants = updatedParticipants

        return viajeRepository.save(viaje)
    }

    fun removeParticipant(viajeId: Long, username: String): Viaje {
        val viaje = findViajeById(viajeId)
        val usuario = usuarioRepository.findByUsername(username)
            .orElseThrow { RuntimeException("User not found") }

        if (!viaje.participants.any { it.id == usuario.id }) {
            throw RuntimeException("User is not a participant")
        }

        val updatedParticipants = viaje.participants.toMutableSet()
        updatedParticipants.removeIf { it.id == usuario.id }
        viaje.participants = updatedParticipants

        return viajeRepository.save(viaje)
    }
}
