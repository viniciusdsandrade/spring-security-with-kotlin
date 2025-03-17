package com.restful.jwt.controller

import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.dto.company.CompanyResponse
import com.restful.jwt.service.CompanyService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/api/company")
class CompanyController(
    private val companyService: CompanyService
) {

    @PostMapping("/create")
    fun createCompany(@Valid @RequestBody companyRequest: CompanyRequest): CompanyResponse =
        companyService.createCompany(companyRequest)

    @GetMapping
    fun listAllCompanies(): List<CompanyResponse> =
        companyService.findAll()

    @GetMapping("/{uuid}")
    fun findCompanyById(@PathVariable uuid: UUID): CompanyResponse =
        companyService.findByUUID(uuid)
            ?: throw ResponseStatusException(NOT_FOUND, "Company not found")

    @DeleteMapping("/{uuid}")
    fun deleteCompanyById(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val success = companyService.deleteByUUID(uuid)
        return if (success) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }
}