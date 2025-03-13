package com.restful.jwt.controller

import com.restful.jwt.dto.employee.EmployeeRequest
import com.restful.jwt.dto.employee.EmployeeResponse
import com.restful.jwt.service.EmployeeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/api/employee")
class EmployeeController(
    private val employeeService: EmployeeService
) {

    @PostMapping("/create")
    fun createEmployee(@Valid @RequestBody employeeRequest: EmployeeRequest): EmployeeResponse =
        employeeService.createEmployee(employeeRequest)

    @GetMapping
    fun listAllEmployees(): List<EmployeeResponse> =
        employeeService.findAll()

    @GetMapping("/{uuid}")
    fun findEmployeeById(@PathVariable uuid: UUID): EmployeeResponse =
        employeeService.findByUUID(uuid)
            ?: throw ResponseStatusException(NOT_FOUND, "Employee not found")

    @DeleteMapping("/{uuid}")
    fun deleteEmployeeById(@PathVariable uuid: UUID): ResponseEntity<Boolean> {
        val success = employeeService.deleteByUUID(uuid)
        return if (success) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
