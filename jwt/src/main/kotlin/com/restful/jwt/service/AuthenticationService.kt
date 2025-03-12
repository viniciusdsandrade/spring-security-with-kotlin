package com.restful.jwt.service

import com.restful.jwt.dto.JwtProperties
import com.restful.jwt.dto.AuthenticationRequest
import com.restful.jwt.dto.AuthenticationResponse
import com.restful.jwt.model.RefreshToken
import com.restful.jwt.repository.RefreshTokenRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.util.Date

@Service("authenticationService")
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
        val refreshToken = generateRefreshToken(user)

        // Persiste o refresh token no banco
        refreshTokenRepository.save(
            RefreshToken(
                token = refreshToken,
                username = user.username
            )
        )

        return AuthenticationResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    private fun generateAccessToken(user: UserDetails) = tokenService.generateAccessToken(
        userDetails = user,
        expirationDate = Date(currentTimeMillis() + jwtProperties.accessTokenExpiration)
    )

    private fun generateRefreshToken(user: UserDetails) = tokenService.generateAccessToken(
        userDetails = user,
        expirationDate = Date(currentTimeMillis() + jwtProperties.refreshTokenExpiration)
    )

    fun refreshAccessToken(token: String): String? {
        val email = tokenService.extractEmail(token)
        val refreshTokenEntity = refreshTokenRepository.findByToken(token)
        val currentUserDetails = userDetailsService.loadUserByUsername(email)

        return if (refreshTokenEntity != null &&
            !tokenService.isExpired(token) &&
            currentUserDetails.username == refreshTokenEntity.username
        ) generateAccessToken(currentUserDetails)
        else null
    }
}
