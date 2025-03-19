package com.restful.jwt.service

// UserServiceImplTest.kt
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.UserRepository
import com.restful.jwt.service.impl.UserServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    lateinit var userService: UserServiceImpl

    @Test
    fun `deve criar usuário quando não existir`() {
        val email = "test@example.com"
        val rawPassword = "password"
        val encodedPassword = "encodedPassword"
        val user = User(id = UUID.randomUUID(), email = email, password = rawPassword, role = Role.USER)

        `when`(userRepository.findByEmail(email)).thenReturn(null)
        `when`(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword)
        val savedUser = user.copy(password = encodedPassword)
        `when`(userRepository.save(any(User::class.java))).thenReturn(savedUser)

        val result = userService.createUser(user)
        assertNotNull(result)
        assertEquals(email, result?.email)
        assertEquals(encodedPassword, result?.password)
        verify(userRepository, times(1)).findByEmail(email)
        verify(userRepository, times(1)).save(any(User::class.java))
    }

    @Test
    fun `não deve criar usuário quando já existir`() {
        val email = "test@example.com"
        val user = User(id = UUID.randomUUID(), email = email, password = "password", role = Role.USER)
        `when`(userRepository.findByEmail(email)).thenReturn(user)

        val result = userService.createUser(user)
        assertNull(result)
        verify(userRepository, times(1)).findByEmail(email)
        verify(userRepository, never()).save(any(User::class.java))
    }

    @Test
    fun `deve encontrar usuário pelo UUID`() {
        val uuid = UUID.randomUUID()
        val user = User(id = uuid, email = "test@example.com", password = "password", role = Role.USER)
        `when`(userRepository.findById(uuid)).thenReturn(Optional.of(user))

        val result = userService.findByUUID(uuid)
        assertNotNull(result)
        assertEquals(uuid, result?.id)
        verify(userRepository, times(1)).findById(uuid)
    }

    @Test
    fun `deve retornar nulo se usuário não existir pelo UUID`() {
        val uuid = UUID.randomUUID()
        `when`(userRepository.findById(uuid)).thenReturn(Optional.empty())

        val result = userService.findByUUID(uuid)
        assertNull(result)
        verify(userRepository, times(1)).findById(uuid)
    }

    @Test
    fun `deve encontrar usuário pelo email`() {
        val email = "test@example.com"
        val user = User(id = UUID.randomUUID(), email = email, password = "password", role = Role.USER)
        `when`(userRepository.findByEmail(email)).thenReturn(user)

        val result = userService.findByEmail(email)
        assertNotNull(result)
        assertEquals(email, result?.email)
        verify(userRepository, times(1)).findByEmail(email)
    }

    @Test
    fun `deve listar todos os usuários`() {
        val user1 = User(id = UUID.randomUUID(), email = "user1@example.com", password = "pass1", role = Role.USER)
        val user2 = User(id = UUID.randomUUID(), email = "user2@example.com", password = "pass2", role = Role.USER)
        `when`(userRepository.findAll()).thenReturn(listOf(user1, user2))

        val result = userService.findAll()
        assertEquals(2, result.size)
        verify(userRepository, times(1)).findAll()
    }

    @Test
    fun `deve deletar usuário quando existir`() {
        val uuid = UUID.randomUUID()
        `when`(userRepository.existsById(uuid)).thenReturn(true)
        doNothing().`when`(userRepository).deleteById(uuid)

        val result = userService.deleteByUUID(uuid)
        assertTrue(result)
        verify(userRepository, times(1)).existsById(uuid)
        verify(userRepository, times(1)).deleteById(uuid)
    }

    @Test
    fun `não deve deletar usuário quando não existir`() {
        val uuid = UUID.randomUUID()
        `when`(userRepository.existsById(uuid)).thenReturn(false)

        val result = userService.deleteByUUID(uuid)
        assertFalse(result)
        verify(userRepository, times(1)).existsById(uuid)
        verify(userRepository, never()).deleteById(uuid)
    }
}
