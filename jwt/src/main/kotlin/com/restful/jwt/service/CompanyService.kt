package com.restful.jwt.service

import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.dto.company.CompanyResponse
import java.util.*
/**
 * Interface para operações de serviço da Company.
 *
 * Essa interface define o contrato para operações sobre a entidade Company, incluindo:
 * - Criação de uma nova empresa (com criação de usuário associado e atribuição da role COMPANY)
 * - Recuperação de todas as empresas
 * - Busca de uma empresa pelo seu UUID
 * - Exclusão de uma empresa pelo seu UUID
 *
 * O design desta interface segue os princípios de programação baseada em interface e implementa
 * o padrão de projeto Strategy. Nesse padrão, a interface especifica "o que" operações estão disponíveis,
 * enquanto a classe que implementa (comumente nomeada CompanyServiceImpl) fornece o comportamento concreto ("como").
 *
 * Essa abordagem adere ao Princípio da Inversão de Dependência, que é um dos princípios SOLID, o que:
 * - Promove o acoplamento fraco entre os consumidores do serviço e as implementações concretas.
 * - Facilita os testes unitários ao permitir o uso de mocks ou stubs para a interface.
 * - Melhora a manutenção ao separar claramente o contrato da sua implementação.
 *
 * Veja também: com.restful.jwt.service.CompanyServiceImpl para a implementação concreta.
 */
interface CompanyService {
    fun createCompany(companyRequest: CompanyRequest): CompanyResponse
    fun findAll(): List<CompanyResponse>
    fun findByUUID(uuid: UUID): CompanyResponse?
    fun deleteByUUID(uuid: UUID): Boolean
}
