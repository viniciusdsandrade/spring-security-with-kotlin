package com.restful.jwt.model

import com.restful.jwt.dto.employee.EmployeeResponse
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
