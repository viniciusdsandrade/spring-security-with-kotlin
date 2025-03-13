package com.restful.jwt.model

import com.restful.jwt.model.enumerated.OrderStatus
import jakarta.persistence.*
import java.util.*
import java.util.UUID.randomUUID

import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.EnumType.STRING

@Entity(name = "Order")
@Table(name = "tb_orders")
data class Order(

    @Id
    @Column(name = "order_id", nullable = false)
    val orderId: UUID = randomUUID(),

    @Column(name = "dt", nullable = false)
    val dt: String,

    @Column(name = "order_creation_shift", nullable = false)
    val orderCreationShift: String,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    val merchant: Merchant,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    val customer: Account,

    @Embedded
    val bag: Bag,

    @Enumerated(STRING)
    @Column(name = "current_status", nullable = false)
    val currentStatus: OrderStatus,

    @Column(name = "review_score")
    val reviewScore: Double?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Order) return false
        return orderId == other.orderId
    }

    override fun hashCode(): Int = orderId.hashCode()
}