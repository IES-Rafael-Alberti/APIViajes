package com.es.apiSpringBoot.repository

import com.es.apiSpringBoot.model.Usuario
import com.es.apiSpringBoot.model.Viaje
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ViajeRepository: JpaRepository<Viaje, Long> {
}