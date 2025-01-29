package com.es.apiSpringBoot.service

import com.es.apiSpringBoot.exception.BadRequestException
import com.es.apiSpringBoot.exception.ConflictException
import com.es.apiSpringBoot.exception.NotFoundException
import com.es.apiSpringBoot.model.Viaje
import com.es.apiSpringBoot.model.enumclasses.MethodOfTravel
import com.es.apiSpringBoot.repository.DestinoRepository
import com.es.apiSpringBoot.repository.UsuarioRepository
import com.es.apiSpringBoot.repository.ViajeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Optional

@Service
class ViajeService {

    @Autowired
    private lateinit var viajeRepository: ViajeRepository

    @Autowired
    private lateinit var destinoRepository: DestinoRepository

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    fun createViaje(viaje: Viaje, idDestino: Long?, idParticipants: Array<Long?>?): Viaje {
        //Comprueba que los datos del viaje sean validos
        validateViaje(viaje, idDestino)

        // Comprueba que el destino exista, si existe lo anade al viaje
        viaje.destination = destinoRepository.findById(idDestino!!)
            .orElseThrow { NotFoundException("El destino no existe")
        }

        // Si hay IDs de participantes, buscarlos y añadirlos
        val validParticipants = idParticipants?.mapNotNull { participantId ->
            participantId?.let { id ->
                usuarioRepository.findById(id)
                    .orElseThrow { NotFoundException("Usuario con ID $id no existe") }
            }
        }?.toSet()

        viaje.participants = validParticipants

        return viajeRepository.save(viaje)
    }

    fun findAllViajes(): List<Viaje>{
            return viajeRepository.findAll()
    }

    fun findViajeById(id: Long): Viaje{
        return viajeRepository.findById(id)
            .orElseThrow{
                NotFoundException("No se encontro el viaje con id ${id}")
            }
    }


    fun updateViaje(id: Long, updatedViaje: Viaje, idDestino: Long?): Viaje {
        validateViaje(updatedViaje, idDestino)

        val existingViaje = viajeRepository.findById(id)
            .orElseThrow { NotFoundException("El viaje que se esta intentando actualizar no existe") }

        destinoRepository.findById(idDestino!!)
            .orElseThrow { NotFoundException("El destino no existe")
        }

        //Los participantes se tienen que cambiar por separado para evitar fallas de seguridad
        updatedViaje.participants = existingViaje.participants

        //Ponemos el mismo id que tenia antes
        updatedViaje.id = existingViaje.id

        return viajeRepository.save(updatedViaje)
    }

    fun deleteViaje(id: Long) {
        val viaje = viajeRepository.findById(id)
            .orElseThrow { NotFoundException("El viaje que se está intentando borrar no existe") }
        viajeRepository.delete(viaje)
    }

    fun findViajesByParticipant(usuarioId: Long): List<Viaje> {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { NotFoundException("Este usuario no existe") }

        return viajeRepository.findAll()
                .filter { it.participants!!.contains(usuario) }
    }



    fun addParticipant(viajeId: Long, username: String): Viaje {
        val viaje = viajeRepository.findById(viajeId)
            .orElseThrow { NotFoundException("No se encuentra el viaje") }
        val usuario = usuarioRepository.findByUsername(username)
            .orElseThrow { NotFoundException("No se encuentra el usuario") }

        if (viaje.participants!!.any { it?.id == usuario.id }) {
            throw ConflictException("User is already a participant")
        }

        val updatedParticipants = viaje.participants!!.toMutableSet()
        updatedParticipants.add(usuario)
        viaje.participants = updatedParticipants

        return viajeRepository.save(viaje)
    }

    fun removeParticipant(viajeId: Long, username: String): Viaje {
        val viaje = viajeRepository.findById(viajeId)
        .orElseThrow { NotFoundException("Este viaje no existe") }
        val usuario = usuarioRepository.findByUsername(username)
            .orElseThrow { NotFoundException("Usuario no existe") }

        if (!viaje.participants!!.any { it?.id == usuario.id }) {
            throw ConflictException("User is not a participant")
        }

        val updatedParticipants = viaje.participants!!.toMutableSet()
        updatedParticipants.removeIf { it?.id == usuario.id }
        viaje.participants = updatedParticipants

        return viajeRepository.save(viaje)
    }


    //Funcion que se utiliza para saber si un usuario pertence a
    fun isUserParticipant(viajeId: Long, usuarioId: Long): Boolean {
        val viaje = viajeRepository.findById(viajeId)
            .orElseThrow { NotFoundException("El viaje no existe") }
        return viaje.participants!!.any { it?.id == usuarioId }
    }

    private fun validateViaje(viaje: Viaje, idDestino: Long?) {
        val errors = mutableListOf<String>()

        // Comprueba que no haya datos nulos
        if(idDestino == null) {
            errors.add("El id de destino no puede estar vacio")
        }
        if (viaje.date == null) {
            errors.add("La fecha no puede estar vacía")
        }else{
            // Comprueba que la fecha sea válida y futura
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                dateFormat.isLenient = false

                val dateStr = dateFormat.format(viaje.date)
                val parsedDate = dateFormat.parse(dateStr)

                if (parsedDate.before(Date())) {
                    errors.add("La fecha del viaje debe ser futura")
                }

                viaje.date = parsedDate
            } catch (e: ParseException) {
                errors.add("El formato de la fecha debe ser: yyyy-MM-dd")
            }
        }
        if (viaje.methodOfTravel == null) {
           errors.add("El método de viaje no puede estar vacío")
        }else{
            // Comprueba que el método de viaje sea válido
            try {
                val validMethod = MethodOfTravel.valueOf(viaje.methodOfTravel.toString())
                if (validMethod !in MethodOfTravel.entries.toTypedArray()) {
                    errors.add("Método de viaje no válido. Métodos válidos: ${MethodOfTravel.entries.joinToString()}")
                }
            } catch (e: IllegalArgumentException) {
                errors.add("Método de viaje no válido. Métodos válidos: ${MethodOfTravel.entries.joinToString()}")
            }
        }

        if (errors.isNotEmpty()) {
            throw BadRequestException(errors.joinToString(". "))
        }
    }
}

