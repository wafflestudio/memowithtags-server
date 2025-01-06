package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<MemoEntity, Long> {
}