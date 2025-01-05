package com.wafflestudio.toyproject.memoWithTags.tag.controller

import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoEntity
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity

data class Tag(
    val id: Long,
    val name: String,
    val color: String,
) {
    companion object {
        fun fromEntity(entity: TagEntity): Tag {
            return Tag(
                id = entity.id!!,
                name = entity.name,
                color = entity.color,
            )
        }
    }
}
