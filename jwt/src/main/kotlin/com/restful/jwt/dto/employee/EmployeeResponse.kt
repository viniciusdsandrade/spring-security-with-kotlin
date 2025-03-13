package com.restful.jwt.dto.employee

import java.util.UUID

data class EmployeeResponse(
    val id: UUID,
    val email: String,
    val name: String
)