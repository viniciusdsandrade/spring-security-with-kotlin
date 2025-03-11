package com.restful.jwt.config

import com.restful.jwt.service.CustomUserDetailsService
import com.restful.jwt.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filtro de autenticação JWT que intercepta as requisições e verifica se o token JWT
 * presente no cabeçalho "Authorization" é válido.
 *
 * O filtro extrai o token do cabeçalho, obtém o email contido no token e, se o token for válido,
 * atualiza o contexto de segurança com os detalhes do usuário autenticado.
 */
@Component
class JwtAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {

    /**
     * Processa a requisição para verificar a presença e validade do token JWT.
     *
     * Se o cabeçalho "Authorization" não contiver um token com o prefixo "Bearer ",
     * o filtro passa a requisição adiante sem alterar o contexto de segurança.
     * Caso contrário, extrai o token, obtém o email e valida o token. Se o token for válido
     * e o usuário existir, o contexto de segurança é atualizado com os detalhes do usuário.
     *
     * @param request objeto HttpServletRequest da requisição
     * @param response objeto HttpServletResponse da resposta
     * @param filterChain cadeia de filtros
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader!!.extractTokenValue()
        val email = tokenService.extractEmail(jwtToken)

        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val foundUser = userDetailsService.loadUserByUsername(email)

            if (tokenService.isValid(jwtToken, foundUser)) {
                updateContext(foundUser, request)
            }
            filterChain.doFilter(request, response)
        }
    }

    /**
     * Atualiza o contexto de segurança com os detalhes do usuário autenticado.
     *
     * Cria um objeto UsernamePasswordAuthenticationToken com os detalhes do usuário e
     * suas autoridades, e atualiza o SecurityContextHolder com este objeto.
     *
     * @param userDetails objeto UserDetails do usuário autenticado
     * @param request objeto HttpServletRequest para extrair detalhes da requisição
     */
    private fun updateContext(userDetails: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authToken
    }

    /**
     * Verifica se o cabeçalho de autorização não contém o prefixo "Bearer ".
     *
     * @return true se o cabeçalho for nulo ou não começar com "Bearer ", false caso contrário.
     */
    private fun String?.doesNotContainBearerToken(): Boolean =
        this == null || !this.startsWith("Bearer ")

    /**
     * Extrai o valor do token JWT removendo o prefixo "Bearer ".
     *
     * @return token JWT sem o prefixo "Bearer ".
     */
    private fun String.extractTokenValue(): String =
        this.replace("Bearer ", "")
}
