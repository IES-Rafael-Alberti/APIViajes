package com.es.apiSpringBoot.controller

import com.es.apiSpringBoot.model.Viaje
import com.es.apiSpringBoot.service.ViajeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.Optional

@RestController
@RequestMapping("/viajes")
class ViajeController {

    @Autowired
    private lateinit var viajeService: ViajeService

    @PostMapping
    fun createViaje(@RequestBody viaje: Viaje): ResponseEntity<Viaje> {
        val createdViaje = viajeService.createViaje(viaje)
        return ResponseEntity(createdViaje, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllViajes(): ResponseEntity<List<Viaje>> {
        val viajes = viajeService.findAllViajes()
        return ResponseEntity.ok(viajes)
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && (hasRole('ADMIN') || @viajeService.isUserParticipant(#id, authentication.principal.id))")
    fun getViajeById(@PathVariable id: Long): ResponseEntity<Optional<Viaje>?> {
        val viaje = viajeService.findViajeById(id)
        return ResponseEntity.ok(viaje)
    }

    @GetMapping("/usuario/{usuarioId}")
    fun getViajesByParticipant(@PathVariable usuarioId: Long): ResponseEntity<List<Viaje>> {
        val viajes = viajeService.findViajesByParticipant(usuarioId)
        return ResponseEntity.ok(viajes)
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() && (hasRole('ADMIN') || @viajeService.isUserParticipant(#id, authentication.principal.id))")
    fun updateViaje(
        @PathVariable id: Long,
        @RequestBody updatedViaje: Viaje
    ): ResponseEntity<Viaje> {
        val viaje = viajeService.updateViaje(id, updatedViaje)
        return ResponseEntity.ok(viaje)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && (hasRole('ADMIN') || @viajeService.isUserParticipant(#id, authentication.principal.id))")
    fun deleteViaje(@PathVariable id: Long): ResponseEntity<Void> {
        viajeService.deleteViaje(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/join")
    @PreAuthorize("isAuthenticated()")
    fun joinViaje(@PathVariable id: Long): ResponseEntity<Viaje> {
        val viaje = viajeService.addParticipant(id, SecurityContextHolder.getContext().authentication.name)
        return ResponseEntity.ok(viaje)
    }

    @PostMapping("/{id}/leave")
    @PreAuthorize("isAuthenticated()")
    fun leaveViaje(@PathVariable id: Long): ResponseEntity<Viaje> {
        val viaje = viajeService.removeParticipant(id, SecurityContextHolder.getContext().authentication.name)
        return ResponseEntity.ok(viaje)
    }
}