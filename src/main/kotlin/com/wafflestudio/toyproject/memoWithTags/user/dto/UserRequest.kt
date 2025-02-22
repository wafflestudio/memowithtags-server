package com.wafflestudio.toyproject.memoWithTags.user.dto

sealed class UserRequest {
    data class RegisterRequest(
        val email: String,
        val password: String,
        val nickname: String
    ) : UserRequest()

    data class VerifyEmailRequest(
        val email: String,
        val verificationCode: String
    ) : UserRequest()

    data class LoginRequest(
        val email: String,
        val password: String
    ) : UserRequest()

    data class ForgotPasswordRequest(
        val email: String
    ) : UserRequest()

    data class UpdatePasswordRequest(
        val originalPassword: String,
        val newPassword: String
    ) : UserRequest()

    data class UpdateNicknameRequest(
        val nickname: String
    ) : UserRequest()

    data class RefreshTokenRequest(
        val refreshToken: String
    ) : UserRequest()
}
