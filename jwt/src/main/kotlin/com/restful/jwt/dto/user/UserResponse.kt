package com.restful.jwt.dto.user

import java.util.UUID

data class UserResponse(
    val uuid: UUID,
    val email: String
)
