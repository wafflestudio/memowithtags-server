package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import java.time.Instant
import java.util.UUID

class User(
    val id: UUID,
    val userNumber: Int,
    val email: String,
    val nickname: String,
    val createdAt: Instant
) {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id!!,
                userNumber = entity.userNumber,
                email = entity.email,
                nickname = entity.nickname,
                createdAt = entity.createdAt
            )
        }
    }
}
