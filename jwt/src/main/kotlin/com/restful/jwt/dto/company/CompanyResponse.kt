package com.restful.jwt.dto.company


data class CompanyResponse(
    val name: String,
    val email: String,
    val phone: String,
    val cnpj: String,
)