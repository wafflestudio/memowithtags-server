package com.wafflestudio.toyproject.memoWithTags.user.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByEmail(username: String): UserEntity?

    fun deleteByVerified(verified: Boolean)

    @Query("SELECT COALESCE(MAX(u.userNumber), 0) FROM users u")
    fun getMaxUserNumber(): Int
}
