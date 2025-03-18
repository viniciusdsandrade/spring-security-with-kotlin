package com.restful.jwt.dto

import com.restful.jwt.model.Address

data class CorreiosResponse(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val bairro: String?,
    val localidade: String?, // corresponde à cidade
    val uf: String?,
    val ibge: String?,
    val gia: String?,
    val ddd: String?,
    val siafi: String?
) {
    // Converte o DTO da API externa para a entidade Address do seu domínio.
    fun toAddress() = Address(
        logradouro = logradouro,
        bairro = bairro,
        cep = cep,
        complemento = complemento,
        cidade = localidade,
        uf = uf,
        numero = null // o número não é retornado pela API
    )
}