package com.restful.jwt.service.impl

import com.restful.jwt.dto.auth.JwtProperties
import com.restful.jwt.service.TokenService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.util.Date

/**
 * Serviço responsável pela geração, extração e validação de tokens JWT.
 * Utiliza a biblioteca io.jsonwebtoken para manipular os tokens e integra as configurações
 * definidas em JwtProperties.
 *
 * @param jwtProperties Propriedades relacionadas ao JWT, incluindo a chave secreta.
 */
@Service
class TokenServiceImpl(
    jwtProperties: JwtProperties
) : TokenService {
    // Chave secreta utilizada para assinar e validar os tokens JWT.
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    override fun generateAccessToken(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClains: Map<String, Any>
    ): String =
        Jwts.builder()
            .claims() // Inicializa os claims do token.
            .subject(userDetails.username) // Define o subject com o username (geralmente o email).
            .issuedAt(Date(currentTimeMillis())) // Define a data de emissão do token.
            .expiration(expirationDate) // Define a data de expiração do token.
            .and() // Retorna ao builder para continuar a configuração.
            .signWith(secretKey) // Assina o token utilizando a chave secreta.
            .compact() // Converte o token para sua representação compactada (String).

    override fun extractEmail(token: String): String =
        getAllClaims(token).subject

    override fun isExpired(token: String): Boolean =
        getAllClaims(token).expiration.before(Date(currentTimeMillis()))

    override fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)
        return email == userDetails.username && !isExpired(token)
    }

    /**
     * Extrai todos os claims do token JWT.
     *
     * Este método realiza o parsing do token utilizando a chave secreta para
     * verificar a assinatura e extrair os claims.
     *
     * @param token Token JWT do qual os claims serão extraídos.
     * @return Objeto Claims contendo todas as informações presentes no token.
     *
     * @note Corrigido o erro de digitação no nome do método para "getAllClaims".
     */
    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }
}
