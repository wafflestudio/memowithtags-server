package com.wafflestudio.toyproject.memoWithTags.tag.controller

import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity

data class Tag(
    val id: Long,
    val name: String,
    val colorHex: String
) {
    companion object {
        fun fromEntity(entity: TagEntity): Tag {
            return Tag(
                id = entity.id!!,
                name = entity.name,
                colorHex = entity.colorHex
            )
        }
    }
}
