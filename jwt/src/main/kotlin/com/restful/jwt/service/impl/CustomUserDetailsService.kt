package com.restful.jwt.service.impl

import com.restful.jwt.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias ApplicationUser = com.restful.jwt.model.security.User

@Service("customUserDetailsService")
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByEmail(username)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("User not found")

    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(email)
            .password(password)
            .roles(role.name)
            .build()
}