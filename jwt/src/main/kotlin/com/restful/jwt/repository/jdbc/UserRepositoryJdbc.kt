package com.restful.jwt.repository.jdbc

import com.restful.jwt.model.enumerated.Role
import com.restful.jwt.model.security.User
import com.restful.jwt.repository.UserRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository("userRepository")
class UserRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
) : UserRepository {

    private val userRowMapper = RowMapper { rs: ResultSet, _: Int ->
        User(
            id = UUID.fromString(rs.getString("id")),
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

    override fun save(user: User): User {
        jdbcTemplate.update(
            "INSERT INTO tb_users (id, email, password, role) VALUES (?, ?, ?, ?)",
            user.id,            // passe o UUID diretamente
            user.email,
            user.password,
            user.role.toString()
        )
        return user
    }

    override fun findAll(): List<User> =
        jdbcTemplate.query("SELECT * FROM tb_users", userRowMapper)

    override fun deleteById(id: UUID): Boolean {
        val rowsAffected = jdbcTemplate.update("DELETE FROM tb_users WHERE id = ?", id.toString())
        return rowsAffected > 0
    }
}
