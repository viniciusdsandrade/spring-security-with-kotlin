package com.restful.jwt.dto.company

import com.restful.jwt.dto.address.AddressResponse


data class CompanyResponse(
    val name: String,
    val email: String,
    val phone: String,
    val cnpj: String,
    val additionalInfo: String? = null,
    val address: AddressResponse? = null
)