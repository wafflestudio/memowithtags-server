package com.wafflestudio.toyproject.memoWithTags.user.dto

sealed class UserRequest {
    data class RegisterRequest(
        val email: String,
        val password: String
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

    data class ResetPasswordRequest(
        val email: String,
        val verificationCode: String,
        val password: String
    ) : UserRequest()

    data class RefreshTokenRequest(
        val refreshToken: String
    ) : UserRequest()
}
