package com.restful.jwt.controller

import com.restful.jwt.dto.user.UserRequest
import com.restful.jwt.dto.user.UserResponse
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import java.util.UUID.randomUUID

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/create")
    fun createUser(@Valid @RequestBody userRequest: UserRequest): UserResponse {
        val createdUser = userService.createUser(userRequest)
            ?: throw ResponseStatusException(BAD_REQUEST, "User already exists")
        return createdUser.toResponse()
    }

    @GetMapping
    fun listAllUsers(): List<UserResponse> =
        userService.findAll().map { it.toResponse() }

    @GetMapping("/{uuid}")
    fun findUserById(@PathVariable uuid: UUID): UserResponse =
        userService.findByUUID(uuid)
            ?.toResponse()
            ?: throw ResponseStatusException(NOT_FOUND, "User not found")

    private fun User.toResponse(): UserResponse =
        UserResponse(
            uuid = this.id,
            email = this.email
        )
}
