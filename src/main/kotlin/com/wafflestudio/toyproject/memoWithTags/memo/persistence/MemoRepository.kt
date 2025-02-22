package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface MemoRepository : JpaRepository<MemoEntity, Long>, MemoRepositoryCustom
