package com.wafflestudio.toyproject.memoWithTags.user.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(username: String): UserEntity?

    fun deleteByVerified(verified: Boolean)

}
