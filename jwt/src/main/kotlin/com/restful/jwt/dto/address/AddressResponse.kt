package com.restful.jwt.dto.address

data class AddressResponse(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val estado: String,
    val regiao: String,
    val ddd: String
)
