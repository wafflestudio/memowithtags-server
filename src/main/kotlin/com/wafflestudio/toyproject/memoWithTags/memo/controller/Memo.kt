package com.wafflestudio.toyproject.memoWithTags.memo.controller

import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoEntity
import com.wafflestudio.toyproject.memoWithTags.tag.controller.Tag
import com.wafflestudio.toyproject.memoWithTags.user.contoller.User
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import java.time.Instant

class Memo(
    val id: Long,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val tags: List<Tag>,
) {
    companion object {
        fun fromEntity(entity: MemoEntity): Memo {
            return Memo(
                id = entity.id!!,
                content = entity.content,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                tags = entity
            )
        }
    }
}