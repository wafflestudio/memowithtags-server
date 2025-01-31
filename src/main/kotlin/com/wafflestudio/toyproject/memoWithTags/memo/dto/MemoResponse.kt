package com.wafflestudio.toyproject.memoWithTags.memo.dto

import java.time.Instant
import java.util.UUID

sealed class MemoResponse {
    data class AddTagResponse(
        val tagId: UUID
    ) : MemoResponse()

    data class CreateMemoResponse(
        val id: Long,
        val content: String,
        val tagIds: List<UUID>,
        val createdAt: Instant,
        val updatedAt: Instant,
        val locked: Boolean
    ) : MemoResponse()

    data class UpdateMemoResponse(
        val id: Long,
        val content: String,
        val tagIds: List<UUID>,
        val createdAt: Instant,
        val updatedAt: Instant,
        val locked: Boolean
    ) : MemoResponse()
}
