package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import com.wafflestudio.toyproject.memoWithTags.memo.dto.SearchResult
import java.time.Instant
import java.util.*

interface MemoRepositoryCustom {
    fun searchMemo(userId: UUID, content: String?, tags: List<Long>?, startDate: Instant?, endDate: Instant?, page: Int, pageSize: Int): SearchResult<Memo>
}
