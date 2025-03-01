package com.restful.jwt.service

import com.restful.jwt.model.User
import com.restful.jwt.repository.UserRepository
import org.springframework.stereotype.Service

@Service("userService")
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(user: User) : User? {
        val found = userRepository.findByEmail(user.email)

        return if (found == null) {
            userRepository.save(user.toString())
            user
        } else null
    }

    fun findByUUID(uuid: String): User? {
        return userRepository.findByUuid(uuid)
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }


}