package com.wafflestudio.toyproject.memoWithTags.user.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/api/v1/auth/register")
    fun register(@RequestBody request: RegisterRequest): RegisterResponse {
        return RegisterResponse("", "", 0L) // 기본값으로 RegisterResponse 반환
    }

    @PostMapping("/api/v1/auth/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        return LoginResponse("", "", 0L) // 기본값으로 LoginResponse 반환
    }

    @PostMapping("/api/v1/auth/verify-email")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest) {
        // 반환 타입이 Unit이므로 아무 작업 없이 비워 둬도 OK
    }

    @PostMapping("/api/v1/auth/forgot-password")
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest) {
        // 반환 타입이 Unit이므로 아무 작업 없이 비워 둬도 OK
    }

    @PostMapping("/api/v1/auth/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest) {
        // 반환 타입이 Unit이므로 아무 작업 없이 비워 둬도 OK
    }

    @PostMapping("/api/v1/auth/refresh-token")
    fun refreshToken(): RefreshTokenResponse {
        return RefreshTokenResponse("", "", 0L) // 기본값으로 RefreshTokenResponse 반환
    }
}

data class RegisterRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)

data class LogoutRequest(
    val Authorization: String,
    val refreshToken: String
)

data class VerifyEmailRequest(
    val email: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class ResetPasswordRequest(
    val email: String,
    val password: String
)

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
