package com.restful.jwt.repository

import com.restful.jwt.dto.user.UserRequest
import com.restful.jwt.model.security.User
import java.util.UUID

interface UserRepository {
    fun findByEmail(email: String): User?
    fun findById(id: UUID): User?
    fun save(user: UserRequest): User
    fun findAll(): List<User>
    fun deleteById(id: UUID): Boolean
}
