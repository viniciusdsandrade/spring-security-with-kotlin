package com.restful.jwt.service

import com.restful.jwt.dto.user.UserRequest
import com.restful.jwt.model.security.User
import java.util.*

interface UserService {
    fun findAll(): List<User>
    fun findByEmail(email: String): User?
    fun findByUUID(id: UUID): User?
    fun createUser(user: UserRequest): User?
}