package com.restful.jwt.service

import com.restful.jwt.dto.auth.AuthenticationRequest
import com.restful.jwt.dto.auth.AuthenticationResponse

interface AuthenticationService {
    fun authentication(authRequest: AuthenticationRequest): AuthenticationResponse
    fun refreshAccessToken(token: String): String?
}