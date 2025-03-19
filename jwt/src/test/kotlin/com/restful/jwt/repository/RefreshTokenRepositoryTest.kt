package com.restful.jwt.repository

import com.restful.jwt.model.security.RefreshToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @DisplayName("deve salvar e encontrar refresh token")
    @Test
    fun should_save_and_find_refresh_token() {
        val token = "sample-token"
        val username = "test@example.com"
        val refreshToken = RefreshToken(token = token, username = username)
        val savedToken = refreshTokenRepository.save(refreshToken)
        assertNotNull(savedToken)
        val foundToken = refreshTokenRepository.findByToken(token)
        assertNotNull(foundToken)
        assertEquals(username, foundToken?.username)
    }
}
