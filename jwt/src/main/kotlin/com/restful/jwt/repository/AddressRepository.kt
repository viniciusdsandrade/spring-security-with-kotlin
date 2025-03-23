package com.restful.jwt.repository

import com.restful.jwt.model.PersistentAddress

interface AddressRepository {
    fun existsByCepAndNumero(cep: String, numero: String): Boolean
    fun findByCepAndNumero(cep: String, numero: String): PersistentAddress?
    fun save(address: PersistentAddress): PersistentAddress
    fun findAll(): List<PersistentAddress>
}