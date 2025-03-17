package com.restful.jwt.service

import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.dto.company.CompanyResponse
import com.restful.jwt.model.Company
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.CompanyRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID

@Service
@Transactional
class CompanyService(
    private val userService: UserService,
    private val companyRepository: CompanyRepository
) {

    fun createCompany(companyRequest: CompanyRequest): CompanyResponse {
        // 1) Cria o objeto User, definindo role como COMPANY
        val user = User(
            id = randomUUID(),
            email = companyRequest.email,
            password = companyRequest.password,
            role = Role.COMPANY
        )

        // 2) Usa o userService para criar o usuário (a senha será encriptada no UserService)
        val savedUser = userService.createUser(user)
            ?: throw IllegalArgumentException("Usuário já existe")

        // 3) Monta a Company com base no CompanyRequest
        val company = Company(
            name = companyRequest.name,
            email = companyRequest.email,
            phone = companyRequest.phone,
            cnpj = companyRequest.cnpj,
            additionalInfo = companyRequest.additionalInfo,
            address = companyRequest.address?.toModel(),
            employees = emptySet() // Inicialmente, a empresa é criada sem funcionários
        )

        // 4) Salva a Company no banco
        val savedCompany = companyRepository.save(company)

        // 5) Retorna a resposta convertida para DTO
        return savedCompany.toResponse()
    }

    /**
     * Retorna todas as Company convertidas para CompanyResponse.
     */
    fun findAll(): List<CompanyResponse> =
        companyRepository.findAll().map { it.toResponse() }

    /**
     * Busca uma Company pelo UUID; caso exista, converte em CompanyResponse.
     * Retorna null se não existir.
     */
    fun findByUUID(uuid: UUID): CompanyResponse? {
        val company = companyRepository.findById(uuid).orElse(null)
        return company?.toResponse()
    }

    /**
     * Deleta uma Company pelo UUID; retorna true se encontrada e removida,
     * ou false se não existir no repositório.
     */
    fun deleteByUUID(uuid: UUID): Boolean {
        return if (companyRepository.existsById(uuid)) {
            companyRepository.deleteById(uuid)
            true
        } else {
            false
        }
    }
}