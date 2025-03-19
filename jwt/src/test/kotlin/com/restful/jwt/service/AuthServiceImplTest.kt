package com.restful.jwt.service

import com.restful.jwt.dto.auth.AuthenticationRequest
import com.restful.jwt.dto.auth.AuthenticationResponse
import com.restful.jwt.dto.auth.JwtProperties
import com.restful.jwt.model.security.RefreshToken
import com.restful.jwt.repository.RefreshTokenRepository
import com.restful.jwt.service.impl.AuthenticationServiceImpl
import com.restful.jwt.service.impl.CustomUserDetailsService
import com.restful.jwt.service.impl.TokenServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any  // Versão Kotlin do any()
import org.mockito.kotlin.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

@ExtendWith(MockitoExtension::class)
class AuthenticationServiceImplTest {

    @Mock
    lateinit var authManager: AuthenticationManager

    @Mock
    lateinit var userDetailsService: CustomUserDetailsService

    @Mock
    lateinit var tokenServiceImpl: TokenServiceImpl

    @Mock
    lateinit var jwtProperties: JwtProperties

    @Mock
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @InjectMocks
    lateinit var authenticationService: AuthenticationServiceImpl

    @DisplayName("deve autenticar usuário com sucesso")
    @Test
    fun should_authenticate_user_successfully() {
        val email = "test@example.com"
        val password = "password"
        val authRequest = AuthenticationRequest(email = email, password = password)
        val userDetails = mock<UserDetails>()
        `when`(userDetails.username).thenReturn(email)
        val accessToken = "access-token"
        val refreshToken = "refresh-token"

        // Cria uma instância dummy de Authentication
        val dummyAuth: Authentication = UsernamePasswordAuthenticationToken(email, password)

        // Simula o retorno da autenticação bem-sucedida
        `when`(authManager.authenticate(any<UsernamePasswordAuthenticationToken>()))
            .thenReturn(dummyAuth)
        `when`(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails)
        // Simula a geração dos tokens usando os matchers do mockito-kotlin
        `when`(tokenServiceImpl.generateAccessToken(any(), any(), any()))
            .thenReturn(accessToken, refreshToken)

        val response: AuthenticationResponse = authenticationService.authentication(authRequest)
        assertNotNull(response)
        assertEquals(accessToken, response.accessToken)
        assertEquals(refreshToken, response.refreshToken)
        // Verifica se o refresh token foi salvo (comentado ou descomentado conforme necessidade)
        // verify(refreshTokenRepository, times(1)).save(any<RefreshToken>())
    }

    @DisplayName("deve atualizar token de acesso com sucesso")
    @Test
    fun should_update_access_token_successfully() {
        val token = "valid-refresh-token"
        val email = "test@example.com"
        val userDetails = mock<UserDetails>()
        `when`(userDetails.username).thenReturn(email)
        val refreshTokenEntity = RefreshToken(token = token, username = email)
        `when`(refreshTokenRepository.findByToken(token)).thenReturn(refreshTokenEntity)
        `when`(tokenServiceImpl.extractEmail(token)).thenReturn(email)
        `when`(tokenServiceImpl.isExpired(token)).thenReturn(false)
        `when`(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails)
        val newAccessToken = "new-access-token"
        `when`(tokenServiceImpl.generateAccessToken(any(), any(), any()))
            .thenReturn(newAccessToken)

        val result = authenticationService.refreshAccessToken(token)
        assertNotNull(result)
        assertEquals(newAccessToken, result)
    }

    @DisplayName("deve retornar nulo ao atualizar token se token for inválido")
    @Test
    fun should_return_null_when_token_is_invalid() {
        val token = "invalid-token"
        val email = "test@example.com"
        `when`(tokenServiceImpl.extractEmail(token)).thenReturn(email)
        `when`(refreshTokenRepository.findByToken(token)).thenReturn(null)

        val result = authenticationService.refreshAccessToken(token)
        assertNull(result)
    }
}
