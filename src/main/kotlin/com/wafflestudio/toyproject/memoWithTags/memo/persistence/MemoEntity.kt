package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.wafflestudio.toyproject.memoWithTags.user.contoller.User
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.Instant

@Entity(name = "memos")
class MemoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "content", nullable = false)
    val content: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
    @Column(name =  "updated_at", nullable = false)
    val updatedAt: Instant,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,

    )

