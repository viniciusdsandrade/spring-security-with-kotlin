package com.restful.jwt.controller

import com.restful.jwt.dto.UserRequest
import com.restful.jwt.dto.UserResponse
import com.restful.jwt.model.Role
import com.restful.jwt.model.User
import com.restful.jwt.service.UserService
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*


@RestController
@RequestMapping("/api/v1/user")
class UserController(

    private val userService: UserService
) {

    @PostMapping("/create")
    fun createUser(@RequestBody userRequest: UserRequest): UserResponse =
        userService.createUser(
            user = userRequest.toModel()
        )
            ?.toResponse()
            ?: throw ResponseStatusException(BAD_REQUEST, "User already exists")


    private fun UserRequest.toModel(): User =
        User(
            id = UUID.randomUUID(),
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