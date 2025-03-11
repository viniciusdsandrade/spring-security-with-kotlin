package com.restful.jwt.repository

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

@Repository("refreshTokenRepository")
class RefreshTokenRepository {
    private val tokens = mutableMapOf<String, UserDetails>()

    fun findUserDetailsByToken(token: String): UserDetails? =
        tokens[token]

    fun save(token: String, userDetails: UserDetails) {
        tokens[token] = userDetails
    }
}