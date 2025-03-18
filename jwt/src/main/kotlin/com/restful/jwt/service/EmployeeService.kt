package com.restful.jwt.service

import com.restful.jwt.dto.employee.EmployeeRequest
import com.restful.jwt.dto.employee.EmployeeResponse
import java.util.*

interface EmployeeService {
    fun deleteByUUID(uuid: UUID): Boolean
    fun findByUUID(uuid: UUID): EmployeeResponse?
    fun findAll(): List<EmployeeResponse>
    fun createEmployee(employeeRequest: EmployeeRequest): EmployeeResponse
}