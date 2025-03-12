package com.restful.jwt.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AuthenticationRequest(
    @field:NotBlank(message = "O email é obrigatório")
    @field:Email(message = "O email deve ter um formato válido")
    val email: String,

    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    val password: String
)
