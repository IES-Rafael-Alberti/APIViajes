package com.es.apiSpringBoot.security

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    private lateinit var rsaKeys: RSAKeysProperties

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { auth -> auth
                // Endpoints publicos
                .requestMatchers(HttpMethod.POST,"/usuarios/login", "/usuarios/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/destinos", "/destinos/{id}").permitAll()

                //Endpoints autorizados
                .requestMatchers(HttpMethod.POST, "/viajes").authenticated()
                .requestMatchers(HttpMethod.PUT,"/viajes/{id}/join").authenticated()
                .requestMatchers(HttpMethod.PUT,"/viajes/{id}/leave").authenticated()


                // Endpoints administrador
                .requestMatchers(HttpMethod.GET,"/usuarios").permitAll()
                .requestMatchers(HttpMethod.GET,"/viajes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/destinos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/destinos/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/destinos/{id}").hasRole("ADMIN")


                // Endpoints de usuario, la logica de estos endpoints esta especificiada con PreAuthorize en los controllers
                //Pueden acceder administradores o los usuarios en si
                .requestMatchers(HttpMethod.GET,"/usuarios/{id}").authenticated()
                .requestMatchers(HttpMethod.PUT,"/usuarios/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").authenticated()

                //Pueden acceder los administradores o los participantes del viaje
                .requestMatchers(HttpMethod.GET,"/viajes/{id}").authenticated()
                .requestMatchers(HttpMethod.PUT,"/viajes/{id}").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/viajes/{id}").authenticated()

                //Default, admin porque nada tendria que acabar aqui, pero si lo hace no quiero que acceda
                //nadie excepto los desarrolladores para solucionarlo
                .anyRequest().hasRole("ADMIN")
            }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic(Customizer.withDefaults())
            .build()
    }

    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * Método que inicializa un objeto de tipo AuthenticationManager
     */
    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration) : AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }


    /*
    MÉTODO PARA CODIFICAR UN JWT
     */
    @Bean
    fun jwtEncoder(): JwtEncoder {
        val jwk: JWK = RSAKey.Builder(rsaKeys.publicKey).privateKey(rsaKeys.privateKey).build()
        val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
        return NimbusJwtEncoder(jwks)
    }

    /*
    MÉTODO PARA DECODIFICAR UN JWT
     */
    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey).build()
    }




}