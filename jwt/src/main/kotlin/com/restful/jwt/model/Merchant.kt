package com.restful.jwt.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*
import java.util.UUID.randomUUID
import jakarta.persistence.Column
import io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY

@Entity(name = "Merchant")
@Table(name = "tb_merchants")
data class Merchant(

    @Id
    @Column(name = "merchant_id", nullable = false)
    val merchantId: UUID = randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(name = "merchant_score")
    val merchantScore: Double?,

    @CreationTimestamp
    @Column(
        name = "created_at",
        columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
        nullable = false,
        updatable = false
    )
    @Schema(
        accessMode = READ_ONLY
    )
    val createdAt: LocalDateTime? = null,

    @Column(name = "main_category")
    val mainCategory: String?,

    @Column(name = "price_range")
    val priceRange: String?,

    @Column(name = "address_district")
    val addressDistrict: String?,

    @Column(name = "address_latitude")
    val addressLatitude: Double?
) {
    @Override
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Merchant) return false
        return merchantId == other.merchantId
    }

    @Override
    override fun hashCode(): Int = merchantId.hashCode()
}