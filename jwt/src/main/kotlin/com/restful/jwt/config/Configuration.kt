package com.restful.jwt.config

import com.restful.jwt.repository.UserRepository
import com.restful.jwt.service.CustomUserDetailsService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Configuração responsável por definir os beans relacionados à autenticação e segurança JWT.
 * Esta classe configura os seguintes beans:
 * - UserDetailsService: Carrega os detalhes do usuário a partir do repositório.
 * - PasswordEncoder: Codifica senhas utilizando o algoritmo BCrypt.
 * - AuthenticationProvider: Provider de autenticação do tipo DAO que integra o UserDetailsService e o PasswordEncoder.
 * - AuthenticationManager: Gerencia o processo de autenticação com base na configuração do Spring Security.
 *
 * @see JwtProperties
 */
@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class Configuration {

    /**
     * Cria um bean do tipo UserDetailsService.
     * Este bean utiliza o CustomUserDetailsService, que implementa a interface UserDetailsService e
     * é responsável por carregar os detalhes do usuário a partir do UserRepository.
     *
     * @param userRepository Repositório responsável pelo acesso aos dados do usuário.
     * @return Uma instância de UserDetailsService.
     */
    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService =
        CustomUserDetailsService(userRepository)

    /**
     * Cria um bean do tipo PasswordEncoder.
     * Este bean utiliza o BCryptPasswordEncoder para codificar as senhas dos usuários, proporcionando uma camada extra de segurança.
     *
     * @return Uma instância de PasswordEncoder.
     */
    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * Cria um bean do tipo AuthenticationProvider.
     * Este provider de autenticação utiliza o DaoAuthenticationProvider, que se integra ao UserDetailsService e PasswordEncoder
     * para realizar a autenticação dos usuários.
     *
     * @param userDetailsService Serviço que carrega os detalhes do usuário.
     * @param encoder Codificador de senhas.
     * @return Uma instância de AuthenticationProvider configurada.
     */
    @Bean
    fun authenticationProvider(
        userDetailsService: UserDetailsService,
        encoder: PasswordEncoder
    ): AuthenticationProvider =
        DaoAuthenticationProvider().also {
            it.setUserDetailsService(userDetailsService)
            it.setPasswordEncoder(encoder)
        }

    /**
     * Cria um bean do tipo AuthenticationManager.
     * O AuthenticationManager é responsável por gerenciar o processo de autenticação, delegando a verificação das credenciais
     * para os providers configurados.
     *
     * @param config Configuração de autenticação do Spring Security.
     * @return Uma instância de AuthenticationManager.
     */
    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}
