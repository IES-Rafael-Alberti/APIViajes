package com.es.apiSpringBoot

import com.es.apiSpringBoot.security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class JwtSecurityKotlinApplication

fun main(args: Array<String>) {
	runApplication<JwtSecurityKotlinApplication>(*args)
}
