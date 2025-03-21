package com.restful.jwt.service.impl

import com.restful.jwt.model.security.User
import com.restful.jwt.repository.UserRepository
import com.restful.jwt.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service("userService")
class UserServiceImpl(
    private val userRepository: UserRepository,  // agora injetado a implementação JDBC
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createUser(user: User): User? {
        val found = userRepository.findByEmail(user.email)
        return if (found == null) {
            val encodedUser = user.copy(password = passwordEncoder.encode(user.password))
            userRepository.save(encodedUser)
        } else null
    }

    override fun findByUUID(id: UUID): User? =
        userRepository.findById(id)

    override fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)

    override fun findAll(): List<User> =
        userRepository.findAll()

    override fun deleteByUUID(id: UUID): Boolean {
        return userRepository.deleteById(id)
    }
}
