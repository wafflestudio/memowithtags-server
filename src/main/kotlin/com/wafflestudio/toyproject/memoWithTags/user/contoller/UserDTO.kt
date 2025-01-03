package com.wafflestudio.toyproject.memoWithTags.user.contoller

import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity

class UserDTO(
    val id: Long,
    val email: String,
    val nickname: String,
) {
    companion object {
        fun fromEntity(entity: UserEntity): UserDTO {
            return UserDTO(
                id = entity.id!!,
                email = entity.email,
                nickname = entity.nickname,
            )
        }
    }
}