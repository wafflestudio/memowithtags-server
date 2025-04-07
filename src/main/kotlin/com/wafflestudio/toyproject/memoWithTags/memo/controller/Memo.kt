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
                    if (memoTagEntity == null) {
                        println(entity.id)
                        println("###############################################################")
                        // 여기서 어떻게 처리할지 결정해야 함
                        null
                    } else {
                        // memoTagEntity 자체는 non-null이라 가정
                        memoTagEntity.tag.id ?: run {
                            println("memoTagEntity.tag가 null입니다!")
                            null
                        }
                    }
                }.filterNotNull(),
                locked = entity.locked
            )
        }
    }
}
