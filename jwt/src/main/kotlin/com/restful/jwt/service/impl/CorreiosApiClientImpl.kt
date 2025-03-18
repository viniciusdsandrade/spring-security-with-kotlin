package com.restful.jwt.service.impl

import com.restful.jwt.dto.correios.CorreiosResponse
import com.restful.jwt.model.Address
import com.restful.jwt.service.CorreiosApiClient
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service("correiosApiClient")
class CorreiosApiClientImpl(
    private val restTemplate: RestTemplate
) : CorreiosApiClient  {

    private val baseUrl = "https://viacep.com.br/ws"

    override fun getAddressByCep(cep: String): Address? {
        val url = "$baseUrl/$cep/json/"
        return try {
            // CorreiosResponse é um DTO que mapeia a resposta da API externa.
            val response = restTemplate.getForObject(url, CorreiosResponse::class.java)
            response?.toAddress()
        } catch (ex: Exception) {
            // Aqui você pode logar o erro e tratar exceções conforme a necessidade.
            null
        }
    }
}