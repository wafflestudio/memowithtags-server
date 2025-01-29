package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.Instant

@Entity(name = "memos")
class MemoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Lob
    @Column(name = "content", nullable = false)
    var content: String,
    @Column(name = "locked", nullable = false)
    var locked: Boolean = false,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @OneToMany(mappedBy = "memo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val memoTags: MutableSet<MemoTagEntity> = mutableSetOf()

)
