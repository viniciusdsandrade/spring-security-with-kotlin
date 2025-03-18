package com.restful.jwt.service

import com.restful.jwt.model.PersistentAddress

interface AddressService {
    fun saveAddress(address: PersistentAddress): PersistentAddress
    fun findAddressByCepAndNumero(cep: String, numero: String): PersistentAddress?
    fun getAllAddresses(): List<PersistentAddress>
}