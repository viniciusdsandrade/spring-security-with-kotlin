package com.restful.jwt.service.impl

import com.restful.jwt.model.PersistentAddress
import com.restful.jwt.repository.AddressRepository
import com.restful.jwt.service.AddressService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service("addressService")
class AddressServiceImpl(
    private val addressRepository: AddressRepository
) : AddressService {

    override fun saveAddress(address: PersistentAddress): PersistentAddress {
        // Verifica se já existe um endereço com o mesmo CEP e número.
        return if (addressRepository.existsByCepAndNumero(address.cep, address.numero)) {
            addressRepository.findByCepAndNumero(address.cep, address.numero)
                ?: addressRepository.save(address) // fallback caso a consulta retorne null, embora não devesse acontecer
        } else {
            addressRepository.save(address)
        }
    }

    override fun findAddressByCepAndNumero(cep: String, numero: String): PersistentAddress? {
        return addressRepository.findByCepAndNumero(cep, numero)
    }

    override fun getAllAddresses(): List<PersistentAddress> {
        return addressRepository.findAll()
    }
}