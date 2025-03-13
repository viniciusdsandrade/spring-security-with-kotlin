package com.restful.jwt.controller

import com.restful.jwt.dto.user.UserRequest
import com.restful.jwt.dto.user.UserResponse
import com.restful.jwt.model.Role
import com.restful.jwt.model.User
import com.restful.jwt.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*
import java.util.UUID.randomUUID

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/create")
    fun createUser(@Valid  @RequestBody userRequest: UserRequest): UserResponse =
        userService.createUser(
            user = userRequest.toModel()
        )
            ?.toResponse()
            ?: throw ResponseStatusException(BAD_REQUEST, "User already exists")

    @GetMapping
    fun listAllUsers(): List<UserResponse> =
        userService.findAll().map { it.toResponse() }

    @GetMapping("/{uuid}")
    fun findUserById(@PathVariable uuid: UUID): UserResponse =
        userService.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(NOT_FOUND, "User not found")

    @DeleteMapping("/{uuid}")
    fun deleteUserById(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val success = userService.deleteByUUID(uuid)
        return if (success) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    private fun UserRequest.toModel(): User =
        User(
            id = randomUUID(),
            email = this.email,
            password = this.password,
            role = Role.USER
        )

    private fun User.toResponse(): UserResponse =
        UserResponse(
            uuid = this.id,
            email = this.email
        )
}