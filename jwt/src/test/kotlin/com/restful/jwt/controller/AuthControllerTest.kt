package com.restful.jwt.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.restful.jwt.dto.auth.AuthenticationRequest
import com.restful.jwt.dto.auth.AuthenticationResponse
import com.restful.jwt.dto.auth.RefreshTokenRequest
import com.restful.jwt.service.AuthenticationService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AuthController::class)
class AuthControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("(deve autenticar usuário com sucesso)")
    fun should_authenticate_user_successfully() {
        val authRequest = AuthenticationRequest(email = "test@example.com", password = "password")
        val authResponse = AuthenticationResponse(accessToken = "access-token", refreshToken = "refresh-token")

        `when`(authenticationService.authentication(any(AuthenticationRequest::class.java))).thenReturn(authResponse)

        mockMvc.perform(
            post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("access-token"))
            .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
    }

    @Test
    @DisplayName("(deve atualizar token de acesso com sucesso)")
    fun should_refresh_access_token_successfully() {
        val refreshRequest = RefreshTokenRequest(token = "valid-refresh-token")
        // Simula que o service retorna um novo access token
        `when`(authenticationService.refreshAccessToken("valid-refresh-token")).thenReturn("new-access-token")

        mockMvc.perform(
            post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value("new-access-token"))
    }

    @Test
    @DisplayName("(deve retornar Forbidden ao atualizar token de acesso com token inválido)")
    fun should_return_forbidden_when_refreshing_access_token_with_invalid_token() {
        val refreshRequest = RefreshTokenRequest(token = "invalid-token")
        `when`(authenticationService.refreshAccessToken("invalid-token")).thenReturn(null)

        mockMvc.perform(
            post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest))
        )
            .andExpect(status().isForbidden)
    }
}
