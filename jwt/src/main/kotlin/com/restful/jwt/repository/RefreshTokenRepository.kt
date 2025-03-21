package com.restful.jwt.repository

import com.restful.jwt.model.security.RefreshToken

interface RefreshTokenRepository {
    fun findByToken(token: String): RefreshToken?
    fun save(refreshToken: RefreshToken): RefreshToken
}
