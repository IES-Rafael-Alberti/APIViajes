## Código para que se pueda copiar y pegar

# SECURITY FILTER CHAIN

````kotlin
/**
     * BEAN QUE ESTABLECE EL SECURITY FILTER CHAIN
     * Método que establece una serie de filtros de seguridad para nuestra API
     * ¿Para qué sirve este método?
     * Este método sirve para establecer los filtros de seguridad que las peticiones deberán cumplir antes de
     * llegar al endpoint al que se dirijan
     *
     * request ------> filtro1 -> filtro2 -> filtro3 ... -> endpoint
     *
     * Por así decirlo, al cargar la aplicación, Spring Security coge lo que tengamos aquí definido y lo "pone" delante de nuestra app a modo de filtros de seguridad. Esto lo hace automáticamente.
     * 
     * 
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain {
        
        return http
            .csrf { csrf -> csrf.disable() } // Deshabilitamos "Cross-Site Request Forgery" (CSRF) (No lo trataremos en este ciclo)
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic(Customizer.withDefaults())
            .build()
    }
````


# JWT DECODER
````kotlin
/**
 * JWTDECODER decodifica un token
 * @return
 */
@Bean
fun jwtDecoder(): JwtDecoder? {
    return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build()
}
````

# JWT ENCODER
````kotlin
/**
 * JWTENCODER codifica un token
 * @return
 */
@Bean
fun jwtEncoder(): JwtEncoder? {
    val jwk: JWK = RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build()
    val jwks: JWKSource<SecurityContext> = ImmutableJWKSet(JWKSet(jwk))
    return NimbusJwtEncoder(jwks)
}
````
