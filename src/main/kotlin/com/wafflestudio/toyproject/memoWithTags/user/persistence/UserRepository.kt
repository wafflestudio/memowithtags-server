package com.wafflestudio.toyproject.memoWithTags.user.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(username: String): UserEntity?

    fun deleteByVerified(verified: Boolean)
}
