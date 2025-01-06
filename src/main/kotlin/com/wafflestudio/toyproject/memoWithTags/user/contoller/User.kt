package com.wafflestudio.toyproject.memoWithTags.user.contoller

import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import java.time.Instant
import java.util.UUID

class User(
    val id: UUID,
    val email: String,
    val nickname: String,
    val createdAt: Instant
) {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id!!,
                email = entity.email,
                nickname = entity.nickname,
                createdAt = entity.createdAt
            )
        }
    }
}
