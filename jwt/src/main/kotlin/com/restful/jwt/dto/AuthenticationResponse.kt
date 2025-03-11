package com.restful.jwt.dto

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)
