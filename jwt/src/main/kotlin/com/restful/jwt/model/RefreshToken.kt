package com.restful.jwt.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity(name = "RefreshToken")
@Table(name = "tb_refresh_tokens")
data class RefreshToken(
    @Id
    val token: String,
    val username: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RefreshToken) return false
        return token == other.token
    }

    override fun hashCode(): Int = token.hashCode()

    override fun toString(): String {
        // Evita expor o token completo; exibindo uma representação mascarada
        return "RefreshToken(token=****, username='$username')"
    }
}