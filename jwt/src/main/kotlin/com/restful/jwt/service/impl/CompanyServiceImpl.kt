package com.restful.jwt.service.impl
import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.dto.company.CompanyResponse
import com.restful.jwt.repository.CompanyRepository
import com.restful.jwt.service.CompanyService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Transactional
@Service("companyService")
class CompanyServiceImpl(
    private val companyRepository: CompanyRepository
) : CompanyService {

    override fun createCompany(companyRequest: CompanyRequest): CompanyResponse {
        // Delegamos a conversão e persistência para o repositório (que utiliza SQL puro)
        val savedCompany = companyRepository.save(companyRequest)
        return savedCompany.toResponse()
    }

    override fun findAll(): List<CompanyResponse> =
        companyRepository.findAll().map { it.toResponse() }

    override fun findByUUID(uuid: UUID): CompanyResponse? {
        val company = companyRepository.findById(uuid)
        return company?.toResponse()
    }
}
