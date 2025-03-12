package com.restful.jwt.service

import com.restful.jwt.model.User
import com.restful.jwt.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service("userService")
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun createUser(user: User): User? {
        val found = userRepository.findByEmail(user.email)
        return if (found == null) {
            // Codifica a senha utilizando BCrypt antes de salvar o usuário
            val encodedUser = user.copy(password = passwordEncoder.encode(user.password))
            userRepository.save(encodedUser)
        } else null
    }

    fun findByUUID(uuid: UUID): User? =
        userRepository.findById(uuid).orElse(null)

    fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)

    fun findAll(): List<User> =
        userRepository.findAll()

    fun deleteByUUID(uuid: UUID): Boolean {
        return if (userRepository.existsById(uuid)) {
            userRepository.deleteById(uuid)
            true
        } else false
    }
}
