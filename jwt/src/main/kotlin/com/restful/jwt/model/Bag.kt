package com.restful.jwt.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

/**
 * Representa a "bag" embutida no pedido, contendo o valor bruto dos itens.
 */
@Embeddable
data class Bag(
    @Column(name = "gross_value", nullable = false)
    val grossValue: Double
)