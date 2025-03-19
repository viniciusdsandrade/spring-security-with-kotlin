package com.restful.jwt.service.impl

import com.restful.jwt.dto.auth.JwtProperties
import com.restful.jwt.dto.auth.AuthenticationRequest
import com.restful.jwt.dto.auth.AuthenticationResponse
import com.restful.jwt.model.security.RefreshToken
import com.restful.jwt.repository.RefreshTokenRepository
import com.restful.jwt.service.AuthenticationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.lang.System.currentTimeMillis
import java.util.Date

@Service("authenticationService")
class AuthenticationServiceImpl(
    private val authManager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenServiceImpl: TokenServiceImpl,
    private val jwtProperties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) : AuthenticationService {

    override fun authentication(authRequest: AuthenticationRequest): AuthenticationResponse {
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

    private fun generateAccessToken(user: UserDetails) = tokenServiceImpl.generateAccessToken(
        userDetails = user,
        expirationDate = Date(currentTimeMillis() + jwtProperties.accessTokenExpiration)
    )

    private fun generateRefreshToken(user: UserDetails) = tokenServiceImpl.generateAccessToken(
        userDetails = user,
        expirationDate = Date(currentTimeMillis() + jwtProperties.refreshTokenExpiration)
    )

    override fun refreshAccessToken(token: String): String? {
        val email = tokenServiceImpl.extractEmail(token)
        val refreshTokenEntity = refreshTokenRepository.findByToken(token)
        val currentUserDetails = userDetailsService.loadUserByUsername(email)

        return if (refreshTokenEntity != null &&
            !tokenServiceImpl.isExpired(token) &&
            currentUserDetails.username == refreshTokenEntity.username
        ) generateAccessToken(currentUserDetails)
        else null
    }
}
