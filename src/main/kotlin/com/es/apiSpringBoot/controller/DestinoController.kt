package com.es.apiSpringBoot.controller

import com.es.apiSpringBoot.controller.DTOClasses.DestinoInput
import com.es.apiSpringBoot.controller.DTOClasses.toFull
import com.es.apiSpringBoot.model.Destino
import com.es.apiSpringBoot.service.DestinoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/destinos")
class DestinoController {

    @Autowired
    private lateinit var destinoService: DestinoService

    @PostMapping
    fun createDestino(@RequestBody destino: DestinoInput): ResponseEntity<Destino> {
        val destinoFull = destino.toFull()
        val createdDestino = destinoService.createDestino(destinoFull)
        return ResponseEntity(createdDestino, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllDestinos(): ResponseEntity<List<Destino>> {
        val destinos = destinoService.findAllDestinos()
        return ResponseEntity.ok(destinos)
    }

    @GetMapping("/{id}")
    fun getDestinoById(@PathVariable id: Long): ResponseEntity<Destino> {
        val destino = destinoService.findDestinoById(id)
        return ResponseEntity.ok(destino)
    }

    @PutMapping("/{id}")
    fun updateDestino(
        @PathVariable id: Long,
        @RequestBody updatedDestino: DestinoInput
    ): ResponseEntity<Destino> {
        val fullDestino = updatedDestino.toFull()
        val destino = destinoService.updateDestino(id, fullDestino)
        return ResponseEntity.ok(destino)
    }

    @DeleteMapping("/{id}")
    fun deleteDestino(@PathVariable id: Long): ResponseEntity<Void> {
        destinoService.deleteDestino(id)
        return ResponseEntity.noContent().build()
    }
}
