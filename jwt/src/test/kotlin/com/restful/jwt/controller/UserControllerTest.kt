package com.restful.jwt.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.restful.jwt.config.JwtAuthenticationFilter
import com.restful.jwt.dto.user.UserRequest
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.service.UserService
import com.restful.jwt.service.impl.CustomUserDetailsService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.randomUUID

@WebMvcTest(
    controllers = [UserController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = [JwtAuthenticationFilter::class])]
)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var customUserDetailsService: CustomUserDetailsService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("(should return BadRequest when user already exists)")
    @WithMockUser
    fun should_return_bad_request_when_user_already_exists() {
        val userRequest = UserRequest(email = "test@example.com", password = "password")

        `when`(userService.createUser(any())).thenReturn(null)

        mockMvc.perform(
            post("/api/user/create")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("(should create user successfully)")
    @WithMockUser
    fun should_create_user_successfully() {
        // Utilize uma senha válida conforme a restrição de validação
        val userRequest = UserRequest(email = "test@example.com", password = "meuLoginGmail2025@")
        // Simulando o usuário criado (note que a senha já deve estar codificada)
        val user = User(id = randomUUID(), email = "test@example.com", password = "encodedPassword", role = Role.USER)

        `when`(userService.createUser(any())).thenReturn(user)

        mockMvc.perform(
            post("/api/user/create")
                .with(csrf())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.uuid").value(user.id.toString()))
            .andExpect(jsonPath("$.email").value(user.email))
    }

    @Test
    @DisplayName("(should list all users)")
    @WithMockUser
    fun should_list_all_users() {
        val user1 = User(id = randomUUID(), email = "user1@example.com", password = "pass1", role = Role.USER)
        val user2 = User(id = randomUUID(), email = "user2@example.com", password = "pass2", role = Role.USER)
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
    @DisplayName("(should find user by UUID)")
    @WithMockUser
    fun should_find_user_by_uuid() {
        val uuid = randomUUID()
        val user = User(id = uuid, email = "find@example.com", password = "pass", role = Role.USER)

        `when`(userService.findByUUID(uuid)).thenReturn(user)

        mockMvc.perform(get("/api/user/{uuid}", uuid))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.uuid").value(uuid.toString()))
            .andExpect(jsonPath("$.email").value(user.email))
    }

    @Test
    @DisplayName("(should return NotFound when user is not found)")
    @WithMockUser
    fun should_return_not_found_when_user_is_not_found() {
        val uuid = randomUUID()

        `when`(userService.findByUUID(uuid)).thenReturn(null)

        mockMvc.perform(get("/api/user/{uuid}", uuid))
            .andExpect(status().isNotFound)
    }

//    @Test
//    @DisplayName("(should delete user successfully)")
//    fun should_delete_user_successfully() {
//        val uuid = randomUUID()
//
//        `when`(userService.deleteByUUID(uuid)).thenReturn(true)
//
//        mockMvc.perform(delete("/api/user/{uuid}", uuid).with(csrf()))
//            .andExpect(status().isNoContent)
//    }
//
//    @Test
//    @DisplayName("(should return NotFound when deleting non-existent user)")
//    fun should_return_not_found_when_deleting_nonexistent_user() {
//        val uuid = randomUUID()
//
//        `when`(userService.deleteByUUID(uuid)).thenReturn(false)
//
//        mockMvc.perform(delete("/api/user/{uuid}", uuid).with(csrf()))
//            .andExpect(status().isNotFound)
//    }
}
