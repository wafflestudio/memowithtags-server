package com.wafflestudio.toyproject.memoWithTags.user.persistence

import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.time.Instant

import java.time.Instant

@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: String? = null,
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "nickname", nullable = false)
    var nickname: String = "Writer",
    @Column(name = "hashed_password", nullable = false)
    var hashedPassword: String,
    @Column(name = "verified", nullable = false)
    var verified: Boolean = false,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val memos: MutableList<MemoEntity> = mutableListOf(),
)
