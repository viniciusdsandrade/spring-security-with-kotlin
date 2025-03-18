package com.restful.jwt.service.impl

import com.restful.jwt.model.security.User
import com.restful.jwt.repository.UserRepository
import com.restful.jwt.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service("userService")
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun createUser(user: User): User? {
        val found = userRepository.findByEmail(user.email)
        return if (found == null) {
            // Codifica a senha utilizando BCrypt antes de salvar o usuário
            val encodedUser = user.copy(password = passwordEncoder.encode(user.password))
            userRepository.save(encodedUser)
        } else null
    }

    override fun findByUUID(uuid: UUID): User? =
        userRepository.findById(uuid).orElse(null)

    override fun findByEmail(email: String): User? =
        userRepository.findByEmail(email)

    override fun findAll(): List<User> =
        userRepository.findAll()

    override fun deleteByUUID(uuid: UUID): Boolean {
        return if (userRepository.existsById(uuid)) {
            userRepository.deleteById(uuid)
            true
        } else false
    }
}
