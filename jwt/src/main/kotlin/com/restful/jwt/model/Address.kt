package com.restful.jwt.model

import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    val cep: String? = null,
    val logradouro: String? = null,
    val numero: String? = null, // Caso seja necessário armazenar o número
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
)
