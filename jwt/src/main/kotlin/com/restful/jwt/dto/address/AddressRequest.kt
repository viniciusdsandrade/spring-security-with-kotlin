package com.restful.jwt.dto.address

import com.restful.jwt.model.Address

data class AddressRequest(
    val cep: String? = null,
    val logradouro: String? = null,
    val numero: String? = null,
    val complemento: String? = null,
    val unidade: String? = null,
    val bairro: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val estado: String? = null,
    val regiao: String? = null,
    val ibge: String? = null,
    val gia: String? = null,
    val ddd: String? = null,
    val siafi: String? = null
) {
    // Converte o DTO de request para a entidade Embeddable Address
    fun toModel() = Address(
        cep = cep,
        logradouro = logradouro,
        numero = numero,
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
