package com.wafflestudio.toyproject.memoWithTags.user.dto

sealed class UserResponse {
    data class LoginResponse(
        val accessToken: String,
        val refreshToken: String
    ) : UserResponse()

    data class RefreshTokenResponse(
        val accessToken: String,
        val refreshToken: String,
        val expiresIn: Long
    ) : UserResponse()
}
