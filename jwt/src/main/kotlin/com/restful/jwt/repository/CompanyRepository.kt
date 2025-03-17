package com.restful.jwt.repository

import com.restful.jwt.model.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository("companyRepository")
interface CompanyRepository : JpaRepository<Company, UUID>