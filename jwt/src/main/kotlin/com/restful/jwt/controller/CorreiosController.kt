package com.restful.jwt.controller

import com.restful.jwt.service.CorreiosApiClient
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cep")
class CorreiosController(
    private val correiosApiClient: CorreiosApiClient
) {
    @GetMapping("/{cep}")
    fun getAddress(@PathVariable cep: String): ResponseEntity<Any> {
        val address = correiosApiClient.getAddressByCep(cep)
        return if (address != null) {
            ResponseEntity.ok(address)
        } else {
            ResponseEntity.status(NOT_FOUND).body("CEP não encontrado")
        }
    }
}
