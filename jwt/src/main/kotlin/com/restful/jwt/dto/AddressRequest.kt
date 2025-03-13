package com.restful.jwt.dto

import com.restful.jwt.model.Address

data class AddressRequest(
    val logradouro: String? = null,
    val bairro: String? = null,
    val cep: String? = null,
    val numero: String? = null,
    val complemento: String? = null,
    val cidade: String? = null,
    val uf: String? = null
) {
    // Converte o DTO de request para a entidade Embeddable Endereco
    fun toModel() = Address(
        logradouro = logradouro,
        bairro = bairro,
        cep = cep,
        numero = numero,
        complemento = complemento,
        cidade = cidade,
        uf = uf
    )
}