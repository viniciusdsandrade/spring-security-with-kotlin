package com.restful.jwt.controller

import com.restful.jwt.model.PersistentAddress
import com.restful.jwt.service.AddressService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/address")
class AddressController(
    private val addressService: AddressService
) {

    // Retorna todos os endereços persistidos
    @GetMapping("/all")
    fun getAllAddresses(): List<PersistentAddress> = addressService.getAllAddresses()

    // Retorna endereços pelo CEP, que é um parâmetro obrigatório na query string
    @GetMapping("/cep")
    fun getAddressByCep(@RequestParam cep: String): ResponseEntity<List<PersistentAddress>> {
        // Busca todos os endereços e filtra pelo CEP
        val addresses = addressService.getAllAddresses().filter { it.cep == cep }
        return if (addresses.isNotEmpty()) ResponseEntity.ok(addresses)
        else ResponseEntity.notFound().build()
    }

    // Retorna um endereço pela combinação de CEP e número
    @GetMapping("/search")
    fun getAddressByCepAndNumero(
        @RequestParam cep: String,
        @RequestParam numero: String
    ): ResponseEntity<PersistentAddress> {
        val address = addressService.findAddressByCepAndNumero(cep, numero)
        return if (address != null) ResponseEntity.ok(address)
        else ResponseEntity.notFound().build()
    }
}
