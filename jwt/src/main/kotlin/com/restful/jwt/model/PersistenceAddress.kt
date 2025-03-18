package com.restful.jwt.model

import jakarta.persistence.*
import java.util.*
import java.util.UUID.randomUUID

@Entity(name = "Address")
@Table(
    name = "tb_addresses",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = [
                "cep",
                "numero"
            ]
        )
    ]
)
data class PersistentAddress(

    @Id
    val id: UUID = randomUUID(),
    val cep: String,
    val logradouro: String,
    val numero: String, // Número é obrigatório para identificar o endereço único
    val complemento: String? = null,
    val unidade: String? = null,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val estado: String,
    val regiao: String,
    val ibge: String,
    val gia: String,
    val ddd: String,
    val siafi: String
) {
    fun toModel(): Address {
        return Address(
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
}