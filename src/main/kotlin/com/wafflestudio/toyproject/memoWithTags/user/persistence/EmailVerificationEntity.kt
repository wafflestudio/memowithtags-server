package com.wafflestudio.toyproject.memoWithTags.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity(name = "emails")
class EmailVerificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "code", nullable = false)
    val code: String,
    @Column(name = "expiryTime", nullable = false)
    val expiryTime: LocalDateTime
)
