package com.restful.jwt.model

import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    val logradouro: String? = null,
    val bairro: String? = null,
    val cep: String? = null,
    val numero: String? = null,
    val complemento: String? = null,
    val cidade: String? = null,
    val uf: String? = null
)