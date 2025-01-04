package com.wafflestudio.toyproject.memoWithTags.user.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "email", nullable = false)
    var email: String,
    @Column(name = "nickname", nullable = false)
    var nickname: String = "Writer",
    @Column(name = "hashed_password", nullable = false)
    var hashedPassword: String
)
