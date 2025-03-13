package com.restful.jwt.dto.employee

import com.restful.jwt.dto.AddressRequest

data class EmployeeRequest(
    val name: String,
    val cpf: String,
    val email: String,
    val password: String,
    val phone: String,
    val additionalInfo: String? = null,
    val address: AddressRequest? = null
)