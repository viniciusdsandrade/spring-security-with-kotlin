package com.restful.jwt.service

import com.restful.jwt.model.Address

interface CorreiosApiClient {
    fun getAddressByCep(cep: String): Address?
}