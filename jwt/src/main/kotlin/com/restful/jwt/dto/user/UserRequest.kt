package com.restful.jwt.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserRequest(
    @field:NotBlank(message = "O email é obrigatório")
    @field:Email(message = "O email deve ter um formato válido")
    val email: String,

    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$",
        message = "A senha deve conter ao menos uma letra maiúscula, uma minúscula, um dígito e um caractere especial"
    )
    val password: String
)
