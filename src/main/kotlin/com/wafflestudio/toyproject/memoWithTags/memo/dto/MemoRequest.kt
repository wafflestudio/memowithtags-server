package com.wafflestudio.toyproject.memoWithTags.memo.dto

import java.time.Instant

sealed class MemoRequest {
    data class CreateMemoRequest(
        val content: String,
        val tagIds: List<Long>
    ) : MemoRequest()
    data class UpdateTagRequest(
        val tagId: Long
    ) : MemoRequest()
    data class UpdateMemoRequest(
        val content: String
    ) : MemoRequest()
    data class MemoSearchRequest(
        val content: String? = null, // 검색할 텍스트 (optional)
        val tagIds: List<Long>? = null, // 태그 ID 배열 (optional, 콤마로 구분된 문자열 처리)
        val startDate: Instant? = null, // 검색 시작 날짜 (optional, ISO 8601 형식)
        val endDate: Instant? = null, // 검색 종료 날짜 (optional, ISO 8601 형식)
        val page: Int // 페이지 번호 (required)
    ) : MemoRequest()
}
