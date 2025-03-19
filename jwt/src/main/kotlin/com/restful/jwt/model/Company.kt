package com.restful.jwt.model

import com.restful.jwt.dto.address.AddressResponse
import com.restful.jwt.dto.company.CompanyResponse
import com.restful.jwt.model.security.User
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
    val employees: Set<Employee> = emptySet(),

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

    fun toResponse(): CompanyResponse = CompanyResponse(
        name = name,
        email = email,
        phone = phone,
        cnpj = cnpj,
        additionalInfo = additionalInfo,
        address = address?.let { addr ->
            AddressResponse(
                cep = addr.cep ?: "",
                logradouro = addr.logradouro ?: "",
                bairro = addr.bairro ?: "",
                localidade = addr.localidade ?: "",
                uf = addr.uf ?: "",
                estado = addr.estado ?: "",
                regiao = addr.regiao ?: "",
                ddd = addr.ddd ?: ""
            )
        }
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Company) return false
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Company(" +
                "id=$id, " +
                "name='$name', " +
                "email='$email', " +
                "phone='$phone', " +
                "cnpj='$cnpj', " +
                "additionalInfo=$additionalInfo, " +
                "address=$address, " +
                "employees=$employees" +
                ")"
    }
}

