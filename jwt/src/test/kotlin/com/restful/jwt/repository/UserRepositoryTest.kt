package com.restful.jwt.repository

import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @DisplayName("deve salvar e encontrar usuário por email")
    @Test
    fun should_save_and_find_user_by_email() {
        val email = "test@example.com"
        val user = User(email = email, password = "password", role = Role.USER)
        val savedUser = userRepository.save(user)
        assertNotNull(savedUser)
        val foundUser = userRepository.findByEmail(email)
        assertNotNull(foundUser)
        assertEquals(savedUser.id, foundUser?.id)
    }

    @DisplayName("deve encontrar usuário pelo id")
    @Test
    fun should_find_user_by_id() {
        val user = User(email = "test2@example.com", password = "password", role = Role.USER)
        val savedUser = userRepository.save(user)
        val foundUser = userRepository.findById(savedUser.id)
        assertTrue(foundUser.isPresent)
        assertEquals(savedUser.email, foundUser.get().email)
    }

    @DisplayName("deve lançar exceção ao salvar usuário com email duplicado")
    @Test
    fun should_throw_exception_when_saving_duplicate_email() {
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
