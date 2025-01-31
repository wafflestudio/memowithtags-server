package com.wafflestudio.toyproject.memoWithTags.tag.persistence

import com.wafflestudio.toyproject.memoWithTags.memo.persistence.EmbeddingVectorConverter
import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoTagEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import java.time.Instant
import java.util.UUID

@Entity(name = "tags")
class TagEntity(
    @Id
    val id: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "color", nullable = false)
    var colorHex: String,

    @Convert(converter = EmbeddingVectorConverter::class)
    @Column(columnDefinition = "TEXT")
    var embeddingVector: List<Double> = emptyList(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant? = null,

    @OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], orphanRemoval = true)
    val memoTags: MutableSet<MemoTagEntity> = mutableSetOf()
)
