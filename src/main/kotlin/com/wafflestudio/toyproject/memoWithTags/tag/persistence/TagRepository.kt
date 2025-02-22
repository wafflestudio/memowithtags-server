package com.wafflestudio.toyproject.memoWithTags.tag.persistence

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<TagEntity, Long> {
    fun findByUserId(userId: UUID): List<TagEntity>
}
