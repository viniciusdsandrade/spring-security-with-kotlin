package com.restful.jwt.repository.jdbc

import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.Employee
import com.restful.jwt.model.Address
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.EmployeeRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID
import java.util.UUID.fromString

@Repository("employeeRepository")
class EmployeeRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
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
                id = UUID.fromString(rs.getString("user_id")),
                email = rs.getString("user_email"),
                password = rs.getString("user_password"),
                role = Role.valueOf(rs.getString("user_role"))
            )
        )
    }

    override fun findByEmail(email: String): Employee? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM tb_employees WHERE email = ?", employeeRowMapper, email)
        } catch (e: Exception) {
            null
        }
    }

    override fun findById(id: UUID): Employee? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM tb_employees WHERE id = ?", employeeRowMapper, id.toString())
        } catch (e: Exception) {
            null
        }
    }

    override fun save(employee: Employee): Employee {
        jdbcTemplate.update(
            """
            INSERT INTO tb_employees (
                id, name, email, phone, cpf, additional_info,
                cep, logradouro, numero, complemento, unidade, bairro,
                localidade, uf, estado, regiao, ibge, gia, ddd, siafi,
                role, user_id, user_email, user_password, user_role
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            employee.id,
            employee.name,
            employee.email,
            employee.phone,
            employee.cpf,
            employee.additionalInfo,
            employee.address?.cep,
            employee.address?.logradouro,
            employee.address?.numero,
            employee.address?.complemento,
            employee.address?.unidade,
            employee.address?.bairro,
            employee.address?.localidade,
            employee.address?.uf,
            employee.address?.estado,
            employee.address?.regiao,
            employee.address?.ibge,
            employee.address?.gia,
            employee.address?.ddd,
            employee.address?.siafi,
            employee.role.toString(),
            employee.user.id,
            employee.user.email,
            employee.user.password,
            employee.user.role.toString()
        )
        return employee
    }

    override fun findAll(): List<Employee> =
        jdbcTemplate.query("SELECT * FROM tb_employees", employeeRowMapper)

}