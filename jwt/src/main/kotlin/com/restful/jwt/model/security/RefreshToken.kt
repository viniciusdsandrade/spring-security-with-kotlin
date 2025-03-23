package com.restful.jwt.model.security

data class RefreshToken(
    val token: String,
    val username: String
)