package com.restful.jwt.repository.jdbc

import com.restful.jwt.dto.employee.EmployeeRequest
import com.restful.jwt.model.enumerated.Role.EMPLOYEE
import com.restful.jwt.model.Employee
import com.restful.jwt.model.Address
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.EmployeeRepository
import com.restful.jwt.service.AddressService
import com.restful.jwt.service.CorreiosApiClient
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID
import java.util.UUID.fromString
import java.util.UUID.randomUUID

@Repository("employeeRepository")
class EmployeeRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val correiosApiClient: CorreiosApiClient,
    private val addressService: AddressService
) : EmployeeRepository {

    private val employeeRowMapper = RowMapper { rs: ResultSet, _: Int ->
        Employee(
            id = fromString(rs.getString("id")),
            name = rs.getString("name"),
            email = rs.getString("email"),
            phone = rs.getString("phone"),
            cpf = rs.getString("cpf"),
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
            role = Role.valueOf(rs.getString("role")),
            user = User(
                id = fromString(rs.getString("user_id")),
                email = rs.getString("user_email"),
                password = rs.getString("user_password"),
                role = Role.valueOf(rs.getString("user_role"))
            )
        )
    }

    override fun findByEmail(email: String): Employee? {
        return try {
            jdbcTemplate.queryForObject(
                """
                SELECT e.*, u.email as user_email, u.password as user_password, u.role as user_role, u.id as user_id
                FROM tb_employees e
                JOIN tb_users u ON e.user_id = u.id
                WHERE e.email = ?
                """.trimIndent(),
                employeeRowMapper,
                email
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun findById(id: UUID): Employee? {
        return try {
            jdbcTemplate.queryForObject(
                """
                SELECT e.*, u.email as user_email, u.password as user_password, u.role as user_role, u.id as user_id
                FROM tb_employees e
                JOIN tb_users u ON e.user_id = u.id
                WHERE e.id = ?
                """.trimIndent(),
                employeeRowMapper,
                id.toString()
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun save(employee: EmployeeRequest): Employee {
        // 1) Cria o usuário associado ao empregado
        val user = User(
            id = randomUUID(),
            email = employee.email,
            password = employee.password,
            role = EMPLOYEE
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
        val finalAddress: Address? = employee.address?.let { addressRequest ->
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

        // 5) Cria o empregado a ser salvo
        val employeeToSave = Employee(
            id = randomUUID(),
            name = employee.name,
            email = employee.email,
            phone = employee.phone,
            cpf = employee.cpf,
            additionalInfo = employee.additionalInfo,
            address = persistedAddress,
            role = EMPLOYEE,
            user = user
        )

        // 4) Insere o empregado na tabela tb_employees
        jdbcTemplate.update(
            """
            INSERT INTO tb_employees (
                id, name, email, phone, cpf, additional_info,
                cep, logradouro, numero, complemento, unidade, bairro,
                localidade, uf, estado, regiao, ibge, gia, ddd, siafi,
                user_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            employeeToSave.id,
            employeeToSave.name,
            employeeToSave.email,
            employeeToSave.phone,
            employeeToSave.cpf,
            employeeToSave.additionalInfo,
            employeeToSave.address?.cep,
            employeeToSave.address?.logradouro,
            employeeToSave.address?.numero,
            employeeToSave.address?.complemento,
            employeeToSave.address?.unidade,
            employeeToSave.address?.bairro,
            employeeToSave.address?.localidade,
            employeeToSave.address?.uf,
            employeeToSave.address?.estado,
            employeeToSave.address?.regiao,
            employeeToSave.address?.ibge,
            employeeToSave.address?.gia,
            employeeToSave.address?.ddd,
            employeeToSave.address?.siafi,
            employeeToSave.user.id
        )
        return employeeToSave
    }

    override fun findAll(): List<Employee> =
        jdbcTemplate.query(
            """
            SELECT e.*, u.email as user_email, u.password as user_password, u.role as user_role, u.id as user_id
            FROM tb_employees e
            JOIN tb_users u ON e.user_id = u.id
            """.trimIndent(),
            employeeRowMapper
        )
}
