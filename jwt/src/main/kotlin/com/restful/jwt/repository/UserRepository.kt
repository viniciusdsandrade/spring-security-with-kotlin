package com.restful.jwt.repository

import com.restful.jwt.model.Role
import com.restful.jwt.model.User
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserRepository {

    private val users = mutableListOf(
        User(
            id = UUID.randomUUID(),
            email = "",
            password = "password",
            role = Role.USER
        ),
        User(
            id = UUID.randomUUID(),
            email = "",
            password = "password",
            role = Role.ADMIN
        )
    )

    fun save(user: User): Boolean {
        return true
    }

    fun findByEmail(email: String): String? {
        return null
    }

    fun findByUuid(uuid: UUID): User? =
        users.firstOrNull { it.id == uuid }


    fun findAll(): List<String> {
        return listOf("User")
    }


}