package com.restful.jwt.dto.correios

import com.restful.jwt.model.Address

data class CorreiosResponse(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val unidade: String?,
    val bairro: String?,
    val localidade: String?, // corresponde à cidade
    val uf: String?,
    val estado: String?,
    val regiao: String?,
    val ibge: String?,
    val gia: String?,
    val ddd: String?,
    val siafi: String?
) {
    // Converte o DTO da API externa para a entidade Address do seu domínio.
    fun toAddress() = Address(
        cep = cep,
        logradouro = logradouro,
        numero = null, // o número não é retornado pela API; será preenchido via request
        complemento = complemento,
        unidade = unidade,
        bairro = bairro,
        localidade = localidade,
        uf = uf,
        estado = estado,
        regiao = regiao,
        ibge = ibge,
        gia = gia,
        ddd = ddd,
        siafi = siafi
    )
}
