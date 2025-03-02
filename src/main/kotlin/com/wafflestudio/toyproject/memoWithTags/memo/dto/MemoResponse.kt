package com.wafflestudio.toyproject.memoWithTags.memo.dto

import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
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

    data class RecommendMemoResponse(
        val memoIds: List<Long>
    ) : MemoResponse()

    data class FetchPageFromMemoResponse(
        val page: Int,
        val totalPages: Int,
        val totalResults: Int,
        val results: List<Memo>
    ) : MemoResponse()
}
