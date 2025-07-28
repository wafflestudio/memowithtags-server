package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import com.wafflestudio.toyproject.memoWithTags.memo.dto.SearchResult
import java.time.Instant
import java.util.*

interface MemoRepositoryCustom {
    fun searchMemo(userId: UUID, content: String?, contentEmbeddingVector: List<Float>?, tags: List<Long>?, startDate: Instant?, endDate: Instant?, page: Int, pageSize: Int, simThreshold: Double? = null, extraTopK: Int = 100): SearchResult<Memo>
}
