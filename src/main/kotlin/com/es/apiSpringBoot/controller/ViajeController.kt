package com.es.apiSpringBoot.controller

import com.es.apiSpringBoot.controller.DTOClasses.ViajeResponse
import com.es.apiSpringBoot.controller.DTOClasses.ViajesInput
import com.es.apiSpringBoot.controller.DTOClasses.toFull
import com.es.apiSpringBoot.controller.DTOClasses.toResponse
import com.es.apiSpringBoot.model.Viaje
import com.es.apiSpringBoot.service.ViajeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import kotlin.collections.map

@RestController
@RequestMapping("/viajes")
class ViajeController {

    @Autowired
    private lateinit var viajeService: ViajeService



    @PostMapping
    fun createViaje(@RequestBody viaje: ViajesInput): ResponseEntity<ViajeResponse> {
        val fullViaje = viaje.toFull()
        val createdViaje = viajeService.createViaje(fullViaje, viaje.idDestination, viaje.participants)
            .toResponse()
        return ResponseEntity(createdViaje, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllViajes(): ResponseEntity<List<ViajeResponse>> {
        val viajes = viajeService.findAllViajes().map { it.toResponse() }
        return ResponseEntity.ok(viajes)
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || @viajeService.isUserParticipant(#id, authentication.name)")
    fun getViajeById(@PathVariable id: Long): ResponseEntity<ViajeResponse> {
        val viaje = viajeService.findViajeById(id).toResponse()
        return ResponseEntity.ok(viaje)
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @viajeService.isUserParticipant(#usuarioId, authentication.name)")
    fun getViajesByParticipant(@PathVariable usuarioId: Long): ResponseEntity<List<ViajeResponse>> {
        val viajes = viajeService.findViajesByParticipant(usuarioId).map { it.toResponse() }
        return ResponseEntity.ok(viajes)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @viajeService.isUserParticipant(#id, authentication.name)")
    fun updateViaje(
        @PathVariable id: Long,
        @RequestBody updatedViaje: ViajesInput
    ): ResponseEntity<ViajeResponse> {
        val fullViaje = updatedViaje.toFull()
        val viaje = viajeService.updateViaje(id, fullViaje, updatedViaje.idDestination).toResponse()
        return ResponseEntity.ok(viaje)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @viajeService.isUserParticipant(#id, authentication.name)")
    fun deleteViaje(@PathVariable id: Long): ResponseEntity<Void> {
        viajeService.deleteViaje(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/join")
    fun joinViaje(@PathVariable id: Long): ResponseEntity<ViajeResponse> {
        val viaje = viajeService.addParticipant(id, SecurityContextHolder.getContext().authentication.name)
            .toResponse()
        return ResponseEntity.ok(viaje)
    }

    @PutMapping("/{id}/leave")
    fun leaveViaje(@PathVariable id: Long): ResponseEntity<ViajeResponse> {
        val viaje = viajeService.removeParticipant(id, SecurityContextHolder.getContext().authentication.name)
            .toResponse()
        return ResponseEntity.ok(viaje)
    }
}