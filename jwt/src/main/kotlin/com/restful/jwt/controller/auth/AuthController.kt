package com.restful.jwt.controller.auth

import com.restful.jwt.dto.AuthenticationRequest
import com.restful.jwt.dto.AuthenticationResponse
import com.restful.jwt.dto.RefreshTokenRequest
import com.restful.jwt.dto.TokenResponse
import com.restful.jwt.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticatinService: AuthenticationService
) {

    @PostMapping
    fun authenticate(@RequestBody authRequest: AuthenticationRequest): AuthenticationResponse =
        authenticatinService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): TokenResponse =
        authenticatinService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token")

    private fun String.mapToTokenResponse(): TokenResponse =
        TokenResponse(
            token = this
        )

}
