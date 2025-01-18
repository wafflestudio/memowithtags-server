package com.wafflestudio.toyproject.memoWithTags.memo.dto

import java.time.Instant

sealed class MemoResponse {
    data class AddTagResponse(
        val tagId: Long
    ) : MemoResponse()

    data class CreateMemoResponse(
        val id: Long,
        val content: String,
        val tagIds: List<Long>,
        val createdAt: Instant,
        val updatedAt: Instant,
        val locked: Boolean
    ) : MemoResponse()

    data class UpdateMemoResponse(
        val id: Long,
        val content: String,
        val tagIds: List<Long>,
        val createdAt: Instant,
        val updatedAt: Instant,
        val locked: Boolean
    ) : MemoResponse()
}
