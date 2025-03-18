package com.restful.jwt.repository

import com.restful.jwt.model.PersistentAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("addressRepository")
interface AddressRepository : JpaRepository<PersistentAddress, UUID> {
    fun existsByCepAndNumero(cep: String, numero: String): Boolean
    fun findByCepAndNumero(cep: String, numero: String): PersistentAddress?
}