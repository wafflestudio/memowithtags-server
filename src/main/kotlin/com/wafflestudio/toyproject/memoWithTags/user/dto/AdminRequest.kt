package com.wafflestudio.toyproject.memoWithTags.user.dto

import java.util.UUID

sealed class AdminRequest {
    data class CreateUserRequest(
        val email: String,
        val password: String,
        val nickname: String
    ) : AdminRequest()

    data class UpdateUserRequest(
        val id: UUID,
        val userUpdateInfo: UserUpdateInfo
    ) : AdminRequest()
}
