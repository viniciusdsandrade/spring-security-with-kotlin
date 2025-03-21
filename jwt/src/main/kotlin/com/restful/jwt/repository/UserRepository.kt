package com.restful.jwt.repository

import com.restful.jwt.model.security.User
import java.util.UUID

interface UserRepository {
    fun findByEmail(email: String): User?
    fun findById(id: UUID): User?
    fun save(user: User): User
    fun findAll(): List<User>
    fun deleteById(id: UUID): Boolean
}
