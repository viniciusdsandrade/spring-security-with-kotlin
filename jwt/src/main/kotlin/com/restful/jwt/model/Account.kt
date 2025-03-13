package com.restful.jwt.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*
import java.util.UUID.randomUUID

@Entity(name = "Account")
@Table(
    name = "tb_accounts",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
data class Account(

    @Id
    @Column(name = "user_id", nullable = false)
    val userId: UUID = randomUUID(),

    @Column(nullable = false)
    val email: String,

    @Column(name = "registration_date", nullable = false)
    val registrationDate: LocalDateTime,

    @Column(nullable = false)
    val name: String,

    @Column(name = "company_id", nullable = false)
    val companyId: String
) {
    @Override
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Account) return false
        return userId == other.userId
    }

    @Override
    override fun hashCode(): Int = userId.hashCode()
}