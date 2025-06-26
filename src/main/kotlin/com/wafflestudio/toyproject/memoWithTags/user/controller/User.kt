package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import java.time.Instant
import java.util.UUID

class User(
    val id: UUID,
    val userNumber: Int,
    val email: String,
    val nickname: String,
    val isSocial: Boolean,
    val createdAt: Instant
) {
    companion object {
        fun fromEntity(entity: UserEntity): User {
            return User(
                id = entity.id!!,
                userNumber = entity.userNumber,
                email = entity.email,
                nickname = entity.nickname,
                isSocial = (entity.socialType != null),
                createdAt = entity.createdAt
            )
        }
    }

    override fun toString(): String {
        return "{id: $id, userNumber: $userNumber, nickname: $nickname, email: $email, isSocial: $isSocial}"
    }
}
