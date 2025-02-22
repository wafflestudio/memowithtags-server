package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemoRepository : JpaRepository<MemoEntity, Long>, MemoRepositoryCustom
