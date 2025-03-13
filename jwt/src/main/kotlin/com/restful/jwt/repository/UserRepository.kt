package com.restful.jwt.repository

import com.restful.jwt.model.security.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository("userRepository")
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
}