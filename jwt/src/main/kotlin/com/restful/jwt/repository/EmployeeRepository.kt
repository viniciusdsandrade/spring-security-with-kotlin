package com.restful.jwt.repository

import com.restful.jwt.model.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("employeeRepository")
interface EmployeeRepository : JpaRepository<Employee, UUID> {
}