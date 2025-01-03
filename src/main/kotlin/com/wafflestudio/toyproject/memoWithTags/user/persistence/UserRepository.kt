package com.wafflestudio.toyproject.memoWithTags.user.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByEmail(username: String): UserEntity?

    fun existsByEmail(username: String): Boolean
}
