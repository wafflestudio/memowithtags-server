package com.wafflestudio.toyproject.memoWithTags.tag.dto

import java.time.Instant
import java.util.UUID

sealed class TagRequest {
    data class CreateTagRequest(
        val id: UUID,
        val name: String,
        val colorHex: String,
        val embeddingVector: List<Float>,
        val createdAt: Instant,
        val updatedAt: Instant?
    ) : TagRequest()

    data class UpdateTagRequest(
        val id: UUID,
        val name: String,
        val colorHex: String,
        val embeddingVector: List<Float>
    ) : TagResponse()
}
