package com.restful.jwt.repository

import com.restful.jwt.model.Employee
import java.util.*

interface EmployeeRepository {
    fun findByEmail(email: String): Employee?
    fun findById(id: UUID): Employee?
    fun save(employee: Employee): Employee
    fun findAll(): List<Employee>
}