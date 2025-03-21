package com.restful.jwt.service

import com.restful.jwt.model.security.User
import java.util.*

interface UserService {
    fun deleteByUUID(id: UUID): Boolean
    fun findAll(): List<User>
    fun findByEmail(email: String): User?
    fun findByUUID(id: UUID): User?
    fun createUser(user: User): User?
}