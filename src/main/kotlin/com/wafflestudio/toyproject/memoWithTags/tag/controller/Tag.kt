package com.wafflestudio.toyproject.memoWithTags.tag.controller

import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import java.time.Instant
import java.util.UUID

data class Tag(
    val id: UUID,
    val name: String,
    val colorHex: String,
    val embeddingVector: List<Double>,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    companion object {
        fun fromEntity(entity: TagEntity): Tag {
            return Tag(
                id = entity.id,
                name = entity.name,
                colorHex = entity.colorHex,
                embeddingVector = entity.embeddingVector,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }
}
