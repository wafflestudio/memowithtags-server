package com.wafflestudio.toyproject.memoWithTags.user.contoller

import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import java.time.Instant

class User(
    val id: Long,
    val email: String,
    val nickname: String,
    val verified: Boolean,
    val createdAt: Instant
) {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id!!,
                email = entity.email,
                nickname = entity.nickname,
                verified = entity.verified,
                createdAt = entity.createdAt
            )
        }
    }
}
