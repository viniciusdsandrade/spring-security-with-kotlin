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
    @Column(name = "id", nullable = false)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PersistentAddress) return false
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "PersistentAddress(" +
                "id=$id, " +
                "cep='$cep', " +
                "logradouro='$logradouro', " +
                "numero='$numero', " +
                "complemento=$complemento, " +
                "unidade=$unidade, " +
                "bairro='$bairro', " +
                "localidade='$localidade', " +
                "uf='$uf', " +
                "estado='$estado', " +
                "regiao='$regiao', " +
                "ibge='$ibge', " +
                "gia='$gia', " +
                "ddd='$ddd', " +
                "siafi='$siafi'" +
                ")"
    }
}