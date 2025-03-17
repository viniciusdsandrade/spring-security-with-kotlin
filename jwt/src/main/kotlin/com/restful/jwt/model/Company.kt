package com.restful.jwt.model

import jakarta.persistence.*
import java.util.*
import java.util.UUID.randomUUID
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.CascadeType.ALL

@Entity(name = "Company")
@Table(
    name = "tb_companies",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["cnpj"])
    ]
)
data class Company(

    @Id
    @Column(name = "id", nullable = false)
    val id: UUID = randomUUID(),

    val name: String,
    val email: String,
    val phone: String,
    val cnpj: String,
    val additionalInfo: String? = null,

    @Embedded
    val address: Address? = null,

    @OneToMany(fetch = LAZY, cascade = [ALL])
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    val employees: Set<Employee> = emptySet()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Company) return false
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

