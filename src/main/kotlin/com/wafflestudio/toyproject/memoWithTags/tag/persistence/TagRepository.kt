package com.wafflestudio.toyproject.memoWithTags.tag.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<TagEntity, Long> {
    fun findByUserId(userId: Long): List<TagEntity>
}
