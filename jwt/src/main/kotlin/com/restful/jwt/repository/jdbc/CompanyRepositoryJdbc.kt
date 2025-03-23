package com.restful.jwt.repository.jdbc

import com.restful.jwt.dto.company.CompanyRequest
import com.restful.jwt.model.Address
import com.restful.jwt.model.Company
import com.restful.jwt.model.PersistentAddress
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.enumerated.Role.COMPANY
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.CompanyRepository
import com.restful.jwt.service.AddressService
import com.restful.jwt.service.CorreiosApiClient
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID
import java.util.UUID.fromString
import java.util.UUID.randomUUID

@Repository("companyRepository")
class CompanyRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val correiosApiClient: CorreiosApiClient,
    private val addressService: AddressService
) : CompanyRepository {

    private val companyRowMapper = RowMapper { rs: ResultSet, _: Int ->
        Company(
            id = fromString(rs.getString("id")),
            name = rs.getString("name"),
            email = rs.getString("email"),
            phone = rs.getString("phone"),
            cnpj = rs.getString("cnpj"),
            additionalInfo = rs.getString("additional_info"),
            address = Address(
                cep = rs.getString("cep"),
                logradouro = rs.getString("logradouro"),
                numero = rs.getString("numero"),
                complemento = rs.getString("complemento"),
                unidade = rs.getString("unidade"),
                bairro = rs.getString("bairro"),
                localidade = rs.getString("localidade"),
                uf = rs.getString("uf"),
                estado = rs.getString("estado"),
                regiao = rs.getString("regiao"),
                ibge = rs.getString("ibge"),
                gia = rs.getString("gia"),
                ddd = rs.getString("ddd"),
                siafi = rs.getString("siafi")
            ),
            employees = emptySet(),
            user = User(
                id = fromString(rs.getString("user_id")),
                email = rs.getString("user_email"),
                password = rs.getString("user_password"),
                role = Role.valueOf(rs.getString("user_role"))
            )
        )
    }

    override fun findByEmail(email: String): Company? {
        return try {
            jdbcTemplate.queryForObject(
                """
                SELECT c.*, u.email as user_email, u.password as user_password, u.role as user_role
                FROM tb_companies c
                JOIN tb_users u ON c.user_id = u.id
                WHERE c.email = ?
                """.trimIndent(),
                companyRowMapper,
                email
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun findById(id: UUID): Company? {
        return try {
            jdbcTemplate.queryForObject(
                """
                SELECT c.*, u.email as user_email, u.password as user_password, u.role as user_role
                FROM tb_companies c
                JOIN tb_users u ON c.user_id = u.id
                WHERE c.id = ?
                """.trimIndent(),
                companyRowMapper,
                id
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun findAll(): List<Company> =
        jdbcTemplate.query(
            """
            SELECT c.*, u.email as user_email, u.password as user_password, u.role as user_role
            FROM tb_companies c
            JOIN tb_users u ON c.user_id = u.id
            """.trimIndent(),
            companyRowMapper
        )

    override fun existsById(id: UUID): Boolean {
        val count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tb_companies WHERE id = ?", Int::class.java, id)
        return count != null && count > 0
    }

    override fun save(companyRequest: CompanyRequest): Company {
        // 1) Cria o usuário associado com role COMPANY
        val user = User(
            id = randomUUID(),
            email = companyRequest.email,
            password = companyRequest.password,
            role = COMPANY
        )

        // 2) Insere o usuário na tabela tb_users
        jdbcTemplate.update(
            "INSERT INTO tb_users (id, email, password, role) VALUES (?, ?, ?, ?)",
            user.id,
            user.email,
            user.password,
            user.role.toString()
        )

        // 3) Chama a API dos Correios para complementar o endereço.
        //    O input possui apenas CEP e número; os demais dados serão obtidos pela API.
        val finalAddress: Address? = companyRequest.address?.let { addressRequest ->
            if (addressRequest.cep != null) {
                val apiAddress = correiosApiClient.getAddressByCep(addressRequest.cep)
                if (apiAddress != null) {
                    // Mescla o número informado no request com os demais dados retornados pela API.
                    apiAddress.copy(numero = addressRequest.numero ?: apiAddress.numero)
                } else {
                    throw IllegalArgumentException("CEP inválido ou endereço não encontrado na API dos Correios")
                }
            } else {
                null
            }
        }

        // 4) Persiste o endereço na tabela tb_addresses usando o AddressService, se houver endereço.
        val persistedAddress: Address? = finalAddress?.let { addr ->
            // Cria um PersistentAddress a partir dos dados do endereço
            val persistent = com.restful.jwt.model.PersistentAddress(
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
            // Salva (ou recupera, se já existir) o endereço persistido
            addressService.saveAddress(persistent).toModel()
        }

        // 5) Monta a entidade Company com o endereço persistido e o usuário associado
        val company = Company(
            id = randomUUID(),
            name = companyRequest.name,
            email = companyRequest.email,
            phone = companyRequest.phone,
            cnpj = companyRequest.cnpj,
            additionalInfo = companyRequest.additionalInfo,
            address = persistedAddress,
            employees = emptySet(),
            user = user
        )

        // 6) Insere a Company na tabela tb_companies, associando-a ao usuário (via user_id)
        jdbcTemplate.update(
            """
            INSERT INTO tb_companies (
                id, name, email, phone, cnpj, additional_info,
                cep, logradouro, numero, complemento, unidade, bairro,
                localidade, uf, estado, regiao, ibge, gia, ddd, siafi,
                user_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            company.id,
            company.name,
            company.email,
            company.phone,
            company.cnpj,
            company.additionalInfo,
            company.address?.cep,
            company.address?.logradouro,
            company.address?.numero,
            company.address?.complemento,
            company.address?.unidade,
            company.address?.bairro,
            company.address?.localidade,
            company.address?.uf,
            company.address?.estado,
            company.address?.regiao,
            company.address?.ibge,
            company.address?.gia,
            company.address?.ddd,
            company.address?.siafi,
            company.user.id
        )
        return company
    }
}
