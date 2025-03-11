package com.restful.jwt.dto

data class AuthenticationRequest(
    val email: String,
    val password: String
)
