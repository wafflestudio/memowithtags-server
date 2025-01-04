package com.wafflestudio.toyproject.memoWithTags.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "nickname", nullable = false)
    var nickname: String = "Writer",
    @Column(name = "hashed_password", nullable = false)
    var hashedPassword: String,
    @Column(name = "verified", nullable = false)
    var verified: Boolean = false,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
)
