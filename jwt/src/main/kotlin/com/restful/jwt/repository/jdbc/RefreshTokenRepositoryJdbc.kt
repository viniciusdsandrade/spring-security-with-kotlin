package com.restful.jwt.repository.jdbc

import com.restful.jwt.model.security.RefreshToken
import com.restful.jwt.repository.RefreshTokenRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository("refreshTokenRepository")
class RefreshTokenRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
) : RefreshTokenRepository {

    private val refreshTokenRowMapper = RowMapper { rs, _: Int ->
        RefreshToken(
            token = rs.getString("token"),
            username = rs.getString("username")
        )
    }

    override fun findByToken(token: String): RefreshToken? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM tb_refresh_tokens WHERE token = ?", refreshTokenRowMapper, token)
        } catch (e: Exception) {
            null
        }
    }

    override fun save(refreshToken: RefreshToken): RefreshToken {
        jdbcTemplate.update(
            "INSERT INTO tb_refresh_tokens (token, username) VALUES (?, ?)",
            refreshToken.token, refreshToken.username
        )
        return refreshToken
    }
}
