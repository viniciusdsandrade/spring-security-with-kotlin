package com.restful.jwt.controller

import com.restful.jwt.dto.AuthenticationRequest
import com.restful.jwt.dto.AuthenticationResponse
import com.restful.jwt.dto.RefreshTokenRequest
import com.restful.jwt.dto.TokenResponse
import com.restful.jwt.service.AuthenticationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping
    fun authenticate(@Valid @RequestBody authRequest: AuthenticationRequest): AuthenticationResponse =
        authenticationService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): TokenResponse =
        authenticationService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResponseStatusException(FORBIDDEN, "Invalid token")

    private fun String.mapToTokenResponse(): TokenResponse =
        TokenResponse(token = this)
}
