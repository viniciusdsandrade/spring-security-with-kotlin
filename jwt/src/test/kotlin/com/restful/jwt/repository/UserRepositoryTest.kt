package com.restful.jwt.repository

// UserRepositoryTest.kt
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `deve salvar e encontrar usuário por email`() {
        val email = "test@example.com"
        val user = User(email = email, password = "password", role = Role.USER)
        val savedUser = userRepository.save(user)
        assertNotNull(savedUser)
        val foundUser = userRepository.findByEmail(email)
        assertNotNull(foundUser)
        assertEquals(savedUser.id, foundUser?.id)
    }

    @Test
    fun `deve encontrar usuário pelo id`() {
        val user = User(email = "test2@example.com", password = "password", role = Role.USER)
        val savedUser = userRepository.save(user)
        val foundUser = userRepository.findById(savedUser.id)
        assertTrue(foundUser.isPresent)
        assertEquals(savedUser.email, foundUser.get().email)
    }

    @Test
    fun `deve lançar exceção ao salvar usuário com email duplicado`() {
        val email = "duplicate@example.com"
        val user1 = User(email = email, password = "password", role = Role.USER)
        userRepository.save(user1)
        val user2 = User(email = email, password = "password", role = Role.USER)
        // Espera-se que a operação lance exceção por violação da constraint única
        assertThrows(Exception::class.java) {
            userRepository.saveAndFlush(user2)
        }
    }
}
