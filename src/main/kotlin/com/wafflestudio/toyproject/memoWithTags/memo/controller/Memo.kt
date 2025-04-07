package com.wafflestudio.toyproject.memoWithTags.memo.controller

import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoEntity
import java.time.Instant

class Memo(
    val id: Long,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val tagIds: List<Long>,
    val locked: Boolean
) {
    companion object {
        fun fromEntity(entity: MemoEntity): Memo {
            return Memo(
                id = entity.id!!,
                content = entity.content,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                tagIds = entity.memoTags.map { memoTagEntity ->
                    memoTagEntity.tag.id
                }.filterNotNull(),
                locked = entity.locked
            )
        }
    }
}
