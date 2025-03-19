package com.restful.jwt.controller

// UserControllerTest.kt
import com.fasterxml.jackson.databind.ObjectMapper
import com.restful.jwt.controller.UserController
import com.restful.jwt.dto.user.UserRequest
import com.restful.jwt.dto.user.UserResponse
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userService: UserService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `deve criar usuário com sucesso`() {
        val userRequest = UserRequest(email = "test@example.com", password = "password")
        // Simulando o usuário criado (note que a senha já deve estar codificada)
        val user = User(id = UUID.randomUUID(), email = "test@example.com", password = "encodedPassword", role = Role.USER)
        val userResponse = UserResponse(uuid = user.id, email = user.email)

        `when`(userService.createUser(any(User::class.java))).thenReturn(user)

        mockMvc.perform(
            post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.uuid").value(user.id.toString()))
            .andExpect(jsonPath("$.email").value(user.email))
    }

    @Test
    fun `deve retornar BadRequest quando usuário já existir`() {
        val userRequest = UserRequest(email = "test@example.com", password = "password")

        `when`(userService.createUser(any(User::class.java))).thenReturn(null)

        mockMvc.perform(
            post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `deve listar todos os usuários`() {
        val user1 = User(id = UUID.randomUUID(), email = "user1@example.com", password = "pass1", role = Role.USER)
        val user2 = User(id = UUID.randomUUID(), email = "user2@example.com", password = "pass2", role = Role.USER)
        val users = listOf(user1, user2)

        `when`(userService.findAll()).thenReturn(users)

        mockMvc.perform(get("/api/user"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].uuid").value(user1.id.toString()))
            .andExpect(jsonPath("$[0].email").value(user1.email))
            .andExpect(jsonPath("$[1].uuid").value(user2.id.toString()))
            .andExpect(jsonPath("$[1].email").value(user2.email))
    }

    @Test
    fun `deve encontrar usuário pelo UUID`() {
        val uuid = UUID.randomUUID()
        val user = User(id = uuid, email = "find@example.com", password = "pass", role = Role.USER)

        `when`(userService.findByUUID(uuid)).thenReturn(user)

        mockMvc.perform(get("/api/user/{uuid}", uuid))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.uuid").value(uuid.toString()))
            .andExpect(jsonPath("$.email").value(user.email))
    }

    @Test
    fun `deve retornar NotFound ao buscar usuário inexistente`() {
        val uuid = UUID.randomUUID()

        `when`(userService.findByUUID(uuid)).thenReturn(null)

        mockMvc.perform(get("/api/user/{uuid}", uuid))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deve deletar usuário com sucesso`() {
        val uuid = UUID.randomUUID()

        `when`(userService.deleteByUUID(uuid)).thenReturn(true)

        mockMvc.perform(delete("/api/user/{uuid}", uuid))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deve retornar NotFound ao tentar deletar usuário inexistente`() {
        val uuid = UUID.randomUUID()

        `when`(userService.deleteByUUID(uuid)).thenReturn(false)

        mockMvc.perform(delete("/api/user/{uuid}", uuid))
            .andExpect(status().isNotFound)
    }
}
