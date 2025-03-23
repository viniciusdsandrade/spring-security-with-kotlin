package com.restful.jwt.model.security

import com.restful.jwt.model.enumerated.Role
import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import java.util.UUID
import java.util.UUID.randomUUID

data class User(
    val id: UUID = randomUUID(),
    val email: String,
    val password: String,
    val role: Role
) {
    override fun toString(): String {
        return "User(id=$id, email='$email', password='[PROTECTED]', role=$role)"
    }
}