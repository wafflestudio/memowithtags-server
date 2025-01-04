package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/auth/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Unit> {
        userService.register(request.email, request.password)
        userService.sendCodeToEmail(request.email)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PostMapping("/auth/verify-email")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest): ResponseEntity<Unit> {
        userService.verifyEmail(request.email, request.verificationCode)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val (_, accessToken, refreshToken) = userService.login(request.email, request.password)
        return ResponseEntity.ok(LoginResponse(accessToken, refreshToken))
    }

    @PostMapping("/auth/forgot-password")
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest) {
        // 반환 타입이 Unit이므로 아무 작업 없이 비워 둬도 OK
    }

    @PostMapping("/auth/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest) {
        // 반환 타입이 Unit이므로 아무 작업 없이 비워 둬도 OK
    }

    @PostMapping("/auth/refresh-token")
    fun refreshToken(): RefreshTokenResponse {
        return RefreshTokenResponse("", "", 0L) // 기본값으로 RefreshTokenResponse 반환
    }
}

data class RegisterRequest(
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)

data class LogoutRequest(
    val Authorization: String,
    val refreshToken: String
)

data class VerifyEmailRequest(
    val email: String,
    val verificationCode: String
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
