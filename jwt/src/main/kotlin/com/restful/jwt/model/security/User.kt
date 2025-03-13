package com.restful.jwt.model.security

import com.restful.jwt.model.enumerated.Role
import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import java.util.UUID
import java.util.UUID.randomUUID

@Entity(name = "User")
@Table(
    name = "tb_users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"])
    ]
)
data class User(

    @Id
    @Column(name = "id", nullable = false)
    val id: UUID = randomUUID(),

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Enumerated(STRING)
    @Column(nullable = false)
    val role: Role
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "User(id=$id, email='$email', role=$role)"
    }
}