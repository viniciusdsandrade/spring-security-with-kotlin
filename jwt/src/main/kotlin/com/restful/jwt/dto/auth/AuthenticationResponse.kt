package com.restful.jwt.dto.auth

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)
