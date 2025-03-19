package com.restful.jwt.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.restful.jwt.config.JwtAuthenticationFilter
import com.restful.jwt.dto.auth.AuthenticationRequest
import com.restful.jwt.dto.auth.AuthenticationResponse
import com.restful.jwt.dto.auth.RefreshTokenRequest
import com.restful.jwt.service.AuthenticationService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [AuthController::class],
    excludeFilters = [Filter(type = ASSIGNABLE_TYPE, value = [JwtAuthenticationFilter::class])]
)
class AuthControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("(deve autenticar usuário com sucesso)")
    @WithMockUser // Se todos os testes exigirem um usuário autenticado, pode ser adicionado aqui
    fun should_authenticate_user_successfully() {
        val authRequest = AuthenticationRequest(email = "test@example.com", password = "password")
        val authResponse = AuthenticationResponse(accessToken = "access-token", refreshToken = "refresh-token")

        `when`(authenticationService.authentication(any<AuthenticationRequest>())).thenReturn(authResponse)

        mockMvc.perform(
            post("/api/auth")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("access-token"))
            .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
    }

    @Test
    @DisplayName("(deve atualizar token de acesso com sucesso)")
    @WithMockUser // Se todos os testes exigirem um usuário autenticado, pode ser adicionado aqui
    fun should_refresh_access_token_successfully() {
        val refreshRequest = RefreshTokenRequest(token = "valid-refresh-token")
        // Simula que o service retorna um novo access token
        `when`(authenticationService.refreshAccessToken("valid-refresh-token")).thenReturn("new-access-token")

        mockMvc.perform(
            post("/api/auth/refresh")
                .with(csrf()) // Adicione esta linha
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value("new-access-token"))
    }


    @Test
    @DisplayName("(deve retornar Forbidden ao atualizar token de acesso com token inválido)")
    @WithMockUser // Se todos os testes exigirem um usuário autenticado, pode ser adicionado aqui
    fun should_return_forbidden_when_refreshing_access_token_with_invalid_token() {
        val refreshRequest = RefreshTokenRequest(token = "invalid-token")
        `when`(authenticationService.refreshAccessToken("invalid-token")).thenReturn(null)

        mockMvc.perform(
            post("/api/auth/refresh")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest))
        )
            .andExpect(status().isForbidden)
    }
}
