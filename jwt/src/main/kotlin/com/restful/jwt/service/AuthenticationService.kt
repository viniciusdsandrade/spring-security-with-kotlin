package com.restful.jwt.service

import com.restful.jwt.config.JwtProperties
import com.restful.jwt.dto.AuthenticationRequest
import com.restful.jwt.dto.AuthenticationResponse
import com.restful.jwt.repository.RefreshTokenRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun authentication(authRequest: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(authRequest.email)

        val accessToken = generateAccessToken(user)
        val refreshToken = generateAccessToken(user)

        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    private fun generateAccessToken(user: UserDetails) = tokenService.generateAccessToken(
        userDetails = user,
        expirationDate = Date(currentTimeMillis() + jwtProperties.refreshTokenExpiration)
    )

    fun refreshAccessToken(token: String): String? {
        val extractedEmail = userDetailsService.loadUserByUsername(tokenService.extractEmail(token))

        return extractedEmail.let { email ->
            val currentUserDetails = userDetailsService.loadUserByUsername(email.toString())
            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(token)

            if (!tokenService.isExpired(token) && currentUserDetails.username == refreshTokenUserDetails?.username)
                generateAccessToken(currentUserDetails)
            else
                null
        }

    }
}