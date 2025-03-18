package com.restful.jwt.service

import com.restful.jwt.model.security.User
import java.util.*

interface UserService {
    fun createUser(user: User): User?
    fun findByUUID(uuid: UUID): User?
    fun findByEmail(email: String): User?
    fun findAll(): List<User>
    fun deleteByUUID(uuid: UUID): Boolean
}