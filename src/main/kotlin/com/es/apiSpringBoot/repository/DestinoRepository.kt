package com.es.apiSpringBoot.repository

import com.es.apiSpringBoot.model.Destino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DestinoRepository: JpaRepository<Destino, Long> {

    fun findByName(name: String) : Optional<Destino>

}