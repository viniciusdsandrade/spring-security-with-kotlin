package com.restful.jwt.repository.jdbc

import com.restful.jwt.model.PersistentAddress
import com.restful.jwt.repository.AddressRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID.fromString

@Repository("addressRepository")
class AddressRepositoryJdbc(
    private val jdbcTemplate: JdbcTemplate
) : AddressRepository {

    private val rowMapper = RowMapper { rs: ResultSet, _: Int ->
        PersistentAddress(
            id = fromString(rs.getString("id")),
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
        )
    }

    override fun existsByCepAndNumero(cep: String, numero: String): Boolean {
        val count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM tb_addresses WHERE cep = ? AND numero = ?",
            Int::class.java, cep, numero
        )
        return count != null && count > 0
    }

    override fun findByCepAndNumero(cep: String, numero: String): PersistentAddress? {
        return try {
            jdbcTemplate.queryForObject(
                "SELECT * FROM tb_addresses WHERE cep = ? AND numero = ?",
                rowMapper, cep, numero
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun save(address: PersistentAddress): PersistentAddress {
        jdbcTemplate.update(
            """
            INSERT INTO tb_addresses 
            (id, cep, logradouro, numero, complemento, unidade, bairro, localidade, uf, estado, regiao, ibge, gia, ddd, siafi) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            address.id,
            address.cep,
            address.logradouro,
            address.numero,
            address.complemento,
            address.unidade,
            address.bairro,
            address.localidade,
            address.uf,
            address.estado,
            address.regiao,
            address.ibge,
            address.gia,
            address.ddd,
            address.siafi
        )
        return address
    }

    override fun findAll(): List<PersistentAddress> =
        jdbcTemplate.query("SELECT * FROM tb_addresses", rowMapper)
}
