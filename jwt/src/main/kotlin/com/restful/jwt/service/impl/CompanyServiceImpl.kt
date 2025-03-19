package com.restful.jwt.service.impl

import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.dto.company.CompanyResponse
import com.restful.jwt.model.Company
import com.restful.jwt.model.PersistentAddress
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.CompanyRepository
import com.restful.jwt.service.CompanyService
import com.restful.jwt.service.CorreiosApiClient // Certifique-se de ter essa classe implementada
import com.restful.jwt.service.AddressService      // Injeção do AddressService
import com.restful.jwt.service.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID

@Transactional
@Service("companyService")
class CompanyServiceImpl(
    private val userService: UserService,
    private val companyRepository: CompanyRepository,
    private val correiosApiClient: CorreiosApiClient, // Injeção da dependência do cliente da API dos Correios
    private val addressService: AddressService        // Injeção do AddressService para persistir endereços
) : CompanyService {

    override fun createCompany(companyRequest: CompanyRequest): CompanyResponse {
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

        // 3) Processa o endereço:
        // O formulário do usuário só permite preencher o CEP e o número.
        // Com o CEP, consulta a API dos Correios para obter os demais dados.
        val finalAddress = companyRequest.address?.let { addressRequest ->
            if (addressRequest.cep != null) {
                // Consulta a API dos Correios com o CEP informado
                val addressFromCorreios = correiosApiClient.getAddressByCep(addressRequest.cep)
                if (addressFromCorreios != null) {
                    // Mescla os dados: usa o CEP informado e o número do request (se houver);
                    // os demais campos são provenientes integralmente da resposta da API.
                    addressFromCorreios.copy(
                        cep = addressRequest.cep,
                        numero = addressRequest.numero ?: addressFromCorreios.numero
                    )
                } else {
                    throw IllegalArgumentException("CEP inválido ou endereço não encontrado na API dos Correios")
                }
            } else {
                null
            }
        }

        // 4) Persiste o endereço utilizando o AddressService, garantindo que não haja duplicidade (mesmo CEP e número)
        val persistentAddress: PersistentAddress? = finalAddress?.let { addr ->
            val newPersistentAddress = PersistentAddress(
                id = randomUUID(),
                cep = addr.cep!!,
                logradouro = addr.logradouro ?: "",
                numero = addr.numero ?: "",
                complemento = addr.complemento,
                unidade = addr.unidade,
                bairro = addr.bairro ?: "",
                localidade = addr.localidade ?: "",
                uf = addr.uf ?: "",
                estado = addr.estado ?: "",
                regiao = addr.regiao ?: "",
                ibge = addr.ibge ?: "",
                gia = addr.gia ?: "",
                ddd = addr.ddd ?: "",
                siafi = addr.siafi ?: ""
            )
            // O AddressService já implementa a lógica de evitar duplicidade.
            addressService.saveAddress(newPersistentAddress)
        }

        // Converte o PersistentAddress para o tipo Address (embeddable) usado na Company
        val address = persistentAddress?.toModel()

        // 5) Monta a Company com base no CompanyRequest, utilizando o endereço persistido e associando o User
        val company = Company(
            name = companyRequest.name,
            email = companyRequest.email,
            phone = companyRequest.phone,
            cnpj = companyRequest.cnpj,
            additionalInfo = companyRequest.additionalInfo,
            address = address,       // Endereço persistido e único
            employees = emptySet(),
            user = savedUser         // Associação one-to-one com o User
        )

        // 6) Salva a Company no banco de dados
        val savedCompany = companyRepository.save(company)

        // 7) Retorna a resposta convertida para DTO
        return savedCompany.toResponse()
    }

    override fun findAll(): List<CompanyResponse> =
        companyRepository.findAll().map { it.toResponse() }

    override fun findByUUID(uuid: UUID): CompanyResponse? {
        val company = companyRepository.findById(uuid).orElse(null)
        return company?.toResponse()
    }

    override fun deleteByUUID(uuid: UUID): Boolean {
        return if (companyRepository.existsById(uuid)) {
            companyRepository.deleteById(uuid)
            true
        } else {
            false
        }
    }
}
