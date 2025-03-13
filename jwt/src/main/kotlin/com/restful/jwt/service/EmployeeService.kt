package com.restful.jwt.service

import com.restful.jwt.dto.employee.EmployeeRequest
import com.restful.jwt.dto.employee.EmployeeResponse
import com.restful.jwt.model.Employee
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.EmployeeRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.UUID.randomUUID

@Service
@Transactional
class EmployeeService(
    private val userService: UserService,
    private val employeeRepository: EmployeeRepository
) {
    fun createEmployee(employeeRequest: EmployeeRequest): EmployeeResponse {
        // 1) Cria o objeto User, definindo role como EMPLOYEE
        val user = User(
            id = randomUUID(),
            email = employeeRequest.email,
            password = employeeRequest.password,
            role = Role.EMPLOYEE
        )

        // 2) Usa o userService para criar o usuário (senha será encriptada no UserService)
        val savedUser = userService.createUser(user)
            ?: throw IllegalArgumentException("Usuário já existe")

        // 3) Monta o Employee com base no EmployeeRequest
        val employee = Employee(
            name = employeeRequest.name,
            cpf = employeeRequest.cpf,
            email = employeeRequest.email,
            phone = employeeRequest.phone,
            additionalInfo = employeeRequest.additionalInfo,
            address = employeeRequest.address?.toModel(),
            user = savedUser
        )

        // 4) Salva o Employee no banco
        val savedEmployee = employeeRepository.save(employee)

        // 5) Retorna a resposta convertida para DTO
        return savedEmployee.toResponse()
    }

    /**
     * Retorna todos os Employees convertidos para EmployeeResponse.
     */
    fun findAll(): List<EmployeeResponse> =
        employeeRepository.findAll().map { it.toResponse() }

    /**
     * Busca um Employee pelo UUID; caso exista, converte em EmployeeResponse.
     * Retorna null se não existir.
     */
    fun findByUUID(uuid: UUID): EmployeeResponse? {
        val employee = employeeRepository.findById(uuid).orElse(null)
        return employee?.toResponse()
    }

    /**
     * Deleta um Employee pelo UUID; retorna true se encontrado e removido,
     * ou false se não existir no repositório.
     */
    fun deleteByUUID(uuid: UUID): Boolean {
        return if (employeeRepository.existsById(uuid)) {
            employeeRepository.deleteById(uuid)
            true
        } else {
            false
        }
    }
}
