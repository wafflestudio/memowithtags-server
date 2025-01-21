package com.wafflestudio.toyproject.memoWithTags.user.persistence

import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoEntity
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.user.RoleType
import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.time.Instant
import java.util.UUID

@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @Column(name = "email", nullable = false)
    val email: String,
    @Column(name = "nickname", nullable = false)
    var nickname: String = "Writer",
    @Column(name = "hashed_password", nullable = false)
    var hashedPassword: String,
    @Column(name = "verified", nullable = false)
    var verified: Boolean = false,
    @Column(name = "role", nullable = false)
    var role: RoleType = RoleType.ROLE_USER,
    @Column(name = "social_type", nullable = true)
    var socialType: SocialType? = null,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var memos: MutableList<MemoEntity> = mutableListOf(),
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tags: MutableList<TagEntity> = mutableListOf()
)
