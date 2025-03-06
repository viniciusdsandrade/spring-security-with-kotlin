package com.restful.jwt.service

import com.restful.jwt.model.User
import com.restful.jwt.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service("userService")
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(user: User) : User? {
        val found = userRepository.findByEmail(user.email)

        return if (found == null) {
            userRepository.save(user)
            user
        } else null
    }

    fun findByUUID(uuid: UUID): User? {
        return userRepository.findByUUID(uuid)
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }


}