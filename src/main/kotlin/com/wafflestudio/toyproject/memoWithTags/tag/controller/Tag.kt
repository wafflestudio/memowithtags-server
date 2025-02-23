package com.wafflestudio.toyproject.memoWithTags.tag.controller

import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import java.time.Instant

data class Tag(
    val id: Long,
    val name: String,
    val colorHex: String,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun fromEntity(entity: TagEntity): Tag {
            return Tag(
                id = entity.id!!,
                name = entity.name,
                colorHex = entity.colorHex,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }
}
