package com.restful.jwt.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Classe de configuração de segurança que define o filtro de segurança para a aplicação.
 *
 * Configura as regras de segurança, incluindo:
 * - Desabilitar o CSRF.
 * - Definir as rotas de acesso público e restrito.
 * - Configurar a política de sessão como STATELESS.
 * - Definir o AuthenticationProvider.
 * - Adicionar o filtro de autenticação JWT antes do filtro padrão de autenticação.
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val authenticationProvider: AuthenticationProvider
) {

    /**
     * Configura o filtro de segurança HTTP.
     *
     * Este metodo configura o objeto HttpSecurity com as seguintes definições:
     * - CSRF desabilitado.
     * - Permite acesso sem autenticação para as rotas "/api/auth", "/api/auth/refresh" e "/error".
     * - Restringe o acesso à rota POST "/api/users" apenas para usuários com a role "ADMIN".
     * - Exige autenticação completa para as demais requisições.
     * - Define a política de sessão como STATELESS.
     * - Configura o AuthenticationProvider.
     * - Adiciona o filtro de autenticação JWT antes do UsernamePasswordAuthenticationFilter.
     *
     * @param http objeto HttpSecurity utilizado para configurar a segurança.
     * @param jwtAuthenticationFilter filtro responsável pela autenticação via JWT.
     * @return o objeto DefaultSecurityFilterChain configurado.
     */
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): DefaultSecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth", "/api/auth/refresh", "/error")
                    .permitAll()
                    .requestMatchers(POST, "/api/user/create")
                    .permitAll() // Permite o acesso a criação de usuário para todos
                    .requestMatchers("/api/user/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .fullyAuthenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}
