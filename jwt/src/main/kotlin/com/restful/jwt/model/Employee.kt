package com.restful.jwt.model

import com.restful.jwt.dto.employee.EmployeeResponse
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import jakarta.persistence.*
import java.util.*
import java.util.UUID.randomUUID
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.FetchType.LAZY

@Entity(name = "Employee")
@Table(
    name = "tb_employees",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"]),
        UniqueConstraint(columnNames = ["cpf"])
    ]
)
data class Employee(

    @Id
    @Column(name = "id", nullable = false)
    val id: UUID = randomUUID(),

    val name: String,
    val email: String,
    val phone: String,
    val cpf: String,
    val additionalInfo: String? = null,

    @Embedded
    val address: Address? = null,

    @Enumerated(STRING)
    val role: Role = Role.EMPLOYEE,

    /**
     * Associação com o usuário que utiliza fetch do tipo LAZY por diversos motivos:
     * - Eficiência de Recursos: Carrega os dados do User apenas quando necessário, evitando consultas desnecessárias.
     * - Melhoria de Performance: Evita o carregamento automático do User em cada consulta de Employee, reduzindo o overhead.
     * - Redução de Problemas N+1: Minimiza o risco de executar múltiplas consultas adicionais para carregar dados não utilizados.
     * - Acoplamento Reduzido: Permite operações com Employee sem depender imediatamente dos detalhes do User.
     */
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User
) {
    fun toResponse(): EmployeeResponse =
        EmployeeResponse(
            id = id,
            name = name,
            email = email
        )
}
