package com.wafflestudio.toyproject.memoWithTags.memo.dto

import java.time.Instant
import java.util.UUID

sealed class MemoResponse {
    data class AddTagResponse(
        val tagId: UUID
    ) : MemoResponse()

    data class CreateMemoResponse(
        val id: UUID,
        val content: String,
        val tagIds: List<UUID>,
        val locked: Boolean,
        val embeddingVector: List<Double>,
        val createdAt: Instant,
        val updatedAt: Instant
    ) : MemoResponse()

    data class UpdateMemoResponse(
        val id: UUID,
        val content: String,
        val tagIds: List<UUID>,
        val locked: Boolean,
        val embeddingVector: List<Double>,
        val createdAt: Instant,
        val updatedAt: Instant
    ) : MemoResponse()
}
