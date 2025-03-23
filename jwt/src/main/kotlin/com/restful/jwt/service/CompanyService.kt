package com.restful.jwt.service

import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.dto.company.CompanyResponse
import java.util.*

interface CompanyService {
    fun createCompany(companyRequest: CompanyRequest): CompanyResponse
    fun findAll(): List<CompanyResponse>
    fun findByUUID(uuid: UUID): CompanyResponse?
}