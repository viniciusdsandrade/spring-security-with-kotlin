package com.restful.jwt.model

import com.restful.jwt.dto.employee.EmployeeResponse
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.enumerated.Role.EMPLOYEE
import com.restful.jwt.model.security.User
import java.util.*
import java.util.UUID.randomUUID

data class Employee(
    val id: UUID = randomUUID(),
    val name: String,
    val email: String,
    val phone: String,
    val cpf: String,
    val additionalInfo: String? = null,
    val address: Address? = null,
    val role: Role = EMPLOYEE,
    /**
     * Associação com o usuário que utiliza fetch do tipo LAZY por diversos motivos:
     * - Eficiência de Recursos: Carrega os dados do User apenas quando necessário, evitando consultas desnecessárias.
     * - Melhoria de Performance: Evita o carregamento automático do User em cada consulta de Employee, reduzindo o overhead.
     * - Redução de Problemas N+1: Minimiza o risco de executar múltiplas consultas adicionais para carregar dados não utilizados.
     * - Acoplamento Reduzido: Permite operações com Employee sem depender imediatamente dos detalhes do User.
     */
    val user: User
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Employee) return false
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Employee(" +
                "id=$id, " +
                "name='$name', " +
                "email='$email', " +
                "phone='$phone', " +
                "cpf='$cpf', " +
                "additionalInfo=$additionalInfo, " +
                "address=$address, " +
                "role=$role, " +
                "user=$user" +
                ")"
    }
}
