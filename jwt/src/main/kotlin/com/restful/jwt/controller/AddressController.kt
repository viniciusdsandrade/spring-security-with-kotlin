package com.restful.jwt.controller

import com.restful.jwt.model.PersistentAddress
import com.restful.jwt.repository.AddressRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/address")
class AddressController(
    private val addressRepository: AddressRepository
) {

    // Retorna todos os endereços persistidos
    @GetMapping("/all")
    fun getAllAddresses(): List<PersistentAddress> = addressRepository.findAll()

    // Retorna um endereço pelo CEP
    // Retorna endereços pelo CEP, que é um parâmetro obrigatório na query string
    @GetMapping("/cep")
    fun getAddressByCep(
        @RequestParam(name = "cep", required = true) cep: String
    ): ResponseEntity<List<PersistentAddress>> {
        val addresses = addressRepository.findAllByCep(cep)
        return if (addresses?.isNotEmpty() == true) ResponseEntity.ok(addresses)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/search")
    fun getAddressByCepAndNumero(
        @RequestParam cep: String,
        @RequestParam numero: String
    ): ResponseEntity<PersistentAddress> {
        val address = addressRepository.findByCepAndNumero(cep, numero)
        return if (address != null) ResponseEntity.ok(address)
        else ResponseEntity.notFound().build()
    }
}