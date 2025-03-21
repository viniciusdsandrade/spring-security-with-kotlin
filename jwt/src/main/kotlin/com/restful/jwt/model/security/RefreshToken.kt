package com.restful.jwt.model.security

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


data class RefreshToken(
    val token: String,
    val username: String
)