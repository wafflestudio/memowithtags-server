package com.wafflestudio.toyproject.memoWithTags.tag.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TagRepository : JpaRepository<TagEntity, Long> {
    fun findByUserId(userId: UUID): List<TagEntity>
}
