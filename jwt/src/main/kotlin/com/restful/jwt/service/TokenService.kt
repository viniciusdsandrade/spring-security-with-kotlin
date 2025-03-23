package com.restful.jwt.service

import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface TokenService {
    fun generateAccessToken(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClains: Map<String, Any> = emptyMap()
    ): String

    fun extractEmail(token: String): String
    fun isExpired(token: String): Boolean
    fun isValid(token: String, userDetails: UserDetails): Boolean
}
