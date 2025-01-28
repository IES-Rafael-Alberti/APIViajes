package com.es.apiSpringBoot.controller

import com.es.apiSpringBoot.controller.trimmedClasses.DestinoTrimmed
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
    fun createDestino(@RequestBody destino: DestinoTrimmed): ResponseEntity<Destino> {
        val fullDestino = Destino(null, destino.name, destino.country)
        val createdDestino = destinoService.createDestino(fullDestino)
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
        @RequestBody updatedDestino: Destino
    ): ResponseEntity<Destino> {
        val destino = destinoService.updateDestino(id, updatedDestino)
        return ResponseEntity.ok(destino)
    }

    @DeleteMapping("/{id}")
    fun deleteDestino(@PathVariable id: Long): ResponseEntity<Void> {
        destinoService.deleteDestino(id)
        return ResponseEntity.noContent().build()
    }
}
