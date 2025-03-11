package com.restful.jwt.repository

import com.restful.jwt.model.Role
import com.restful.jwt.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.UUID.randomUUID

@Repository
class UserRepository(
    private val encoder: PasswordEncoder
) {

    private val users = mutableListOf(
        User(
            id = randomUUID(),
            email = "",
            password = encoder.encode("password"),
            role = Role.USER
        ),
        User(
            id = randomUUID(),
            email = "",
            password = encoder.encode("password"),
            role = Role.ADMIN
        )
    )

    fun save(user: User): Boolean {
        val updated = user.copy(password = encoder.encode(user.password))
        return users.add(updated)
    }

    fun findByEmail(email: String): User? {
        return users.firstOrNull { it.email == email }
    }

    fun findByUUID(uuid: UUID): User? =
        users.firstOrNull { it.id == uuid }


    fun findAll(): List<User> {
        return users
    }

    fun deleteByUUID(uuid: UUID): Boolean {
        val foundUser = findByUUID(uuid)

        return if (foundUser != null) {
            users.remove(foundUser)
            true
        } else false
    }


}