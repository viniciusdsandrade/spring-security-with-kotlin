package com.restful.jwt.repository.jdbc

import com.restful.jwt.dto.user.UserRequest
import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID
import java.util.UUID.fromString
import java.util.UUID.randomUUID

@Repository("userRepository")
class UserRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
) : UserRepository {

    private val userRowMapper = RowMapper { rs: ResultSet, _: Int ->
        User(
            id = fromString(rs.getString("id")),
            email = rs.getString("email"),
            password = rs.getString("password"),
            role = Role.valueOf(rs.getString("role"))
        )
    }

    override fun findByEmail(email: String): User? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM tb_users WHERE email = ?", userRowMapper, email)
        } catch (e: Exception) {
            null
        }
    }

    override fun findById(id: UUID): User? {
        return try {
            jdbcTemplate.queryForObject("SELECT * FROM tb_users WHERE id = ?", userRowMapper, id.toString())
        } catch (e: Exception) {
            null
        }
    }

    override fun save(user: UserRequest): User {
        val id = randomUUID()
        val defaultRole = Role.USER

        jdbcTemplate.update(
            "INSERT INTO tb_users (id, email, password, role) VALUES (?, ?, ?, ?)",
            id, // passa o UUID diretamente
            user.email,
            user.password,
            defaultRole.toString()
        )

        return User(
            id = id,
            email = user.email,
            password = user.password,
            role = defaultRole
        )
    }


    override fun findAll(): List<User> =
        jdbcTemplate.query("SELECT * FROM tb_users", userRowMapper)

    override fun deleteById(id: UUID): Boolean {
        val rowsAffected = jdbcTemplate.update("DELETE FROM tb_users WHERE id = ?", id.toString())
        return rowsAffected > 0
    }
}
