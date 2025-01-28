package com.wafflestudio.toyproject.memoWithTags.user.dto

import java.util.UUID

sealed class AdminRequest {
    data class CreateUserRequest(
        val email: String,
        val nickname: String,
        val password: String
    ) : AdminRequest()

    data class UpdateUserRequest(
        val id: UUID,
        val userUpdateInfo: UserUpdateInfo
    ) : AdminRequest()
}
