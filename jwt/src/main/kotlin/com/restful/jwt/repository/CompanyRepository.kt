package com.restful.jwt.repository

import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.model.Company
import java.util.*

interface CompanyRepository {
    fun findByEmail(email: String): Company?
    fun findById(id: UUID): Company?
    fun findAll(): List<Company>
    fun existsById(id: UUID): Boolean
    fun save(company: CompanyRequest): Company
}