package com.wafflestudio.toyproject.memoWithTags.memo.dto

class SearchResult<T>(
    val page: Int, // 현재 페이지 번호
    val totalPages: Int,
    val totalResults: Int,
    val results: List<T> // 결과 리스트 (제네릭으로 처리)
)
