package com.restful.jwt.service

import com.restful.jwt.dto.JwtProperties
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
class TokenService(
    jwtProperties: JwtProperties
) {
    // Chave secreta utilizada para assinar e validar os tokens JWT.
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    /**
     * Gera um token JWT para o usuário especificado.
     *
     * @param userDetails Detalhes do usuário (utilizado para definir o subject do token).
     * @param expirationDate Data de expiração do token.
     * @param additionalClains Mapa opcional de claims adicionais a serem incluídos no token.
     *                         Nota: Este parâmetro não está sendo utilizado na implementação atual.
     * @return Token JWT gerado.
     */
    fun generateAccessToken(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClains: Map<String, Any> = emptyMap()
    ): String =
        Jwts.builder()
            .claims() // Inicializa os claims do token.
            .subject(userDetails.username) // Define o subject com o username (geralmente o email).
            .issuedAt(Date(currentTimeMillis())) // Define a data de emissão do token.
            .expiration(expirationDate) // Define a data de expiração do token.
            .and() // Retorna ao builder para continuar a configuração.
            .signWith(secretKey) // Assina o token utilizando a chave secreta.
            .compact() // Converte o token para sua representação compactada (String).

    /**
     * Extrai o email (subject) do token JWT.
     *
     * @param token Token JWT do qual o email será extraído.
     * @return Email (subject) extraído dos claims do token.
     */
    fun extractEmail(token: String): String =
        getAllClains(token) // Extrai todos os claims do token.
            .subject     // Retorna o subject que representa o email.

    /**
     * Verifica se o token JWT está expirado.
     *
     * @param token Token JWT a ser verificado.
     * @return `true` se o token estiver expirado; caso contrário, `false`.
     */
    fun isExpired(token: String): Boolean =
        getAllClains(token)
            .expiration                   // Obtém a data de expiração do token.
            .before(Date(currentTimeMillis())) // Compara com a data atual.

    /**
     * Valida se o token JWT é válido para o usuário especificado.
     *
     * O token é considerado válido se:
     * 1. O subject (email) do token corresponder ao username do usuário.
     * 2. O token não estiver expirado.
     *
     * @param token Token JWT a ser validado.
     * @param userDetails Detalhes do usuário para comparação com o token.
     * @return `true` se o token for válido; caso contrário, `false`.
     */
    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)
        return email == userDetails.username && !isExpired(token)
    }

    /**
     * Extrai todos os claims do token JWT.
     *
     * Este metodo realiza o parsing do token utilizando a chave secreta para
     * verificar a assinatura e extrair os claims.
     *
     * @param token Token JWT do qual os claims serão extraídos.
     * @return Objeto Claims contendo todas as informações presentes no token.
     *
     * @note Existe um erro de digitação no nome do metodo e no parâmetro "Clains".
     *       O correto seria "Claims".
     */
    private fun getAllClains(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }
}
