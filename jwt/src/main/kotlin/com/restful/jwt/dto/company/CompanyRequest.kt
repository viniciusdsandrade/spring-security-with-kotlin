package com.restful.jwt.dto.company

import com.restful.jwt.dto.AddressRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CNPJ

data class CompanyRequest(

    @field:NotBlank(message = "O nome não pode estar em branco.")
    val name: String,

    @field:Email(message = "E-mail inválido.")
    @field:NotBlank(message = "O e-mail não pode estar em branco.")
    val email: String,

    @field:NotBlank(message = "A senha não pode estar em branco.")
    @field:Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    val password: String,

    @field:Pattern(
        regexp = "^((\\+55)\\d{11}|\\d{10,11})\$",
        message = "Telefone inválido. Formatos válidos incluem +5519974133884 ou 19974133884."
    )
    val phone: String,

    @field:NotBlank(message = "O CNPJ não pode estar em branco.")
    @CNPJ(message = "CNPJ inválido.")
    val cnpj: String,

    @field:Size(max = 255, message = "Informações adicionais não podem exceder 255 caracteres.")
    val additionalInfo: String? = null,

    @field:Valid
    val address: AddressRequest? = null
)