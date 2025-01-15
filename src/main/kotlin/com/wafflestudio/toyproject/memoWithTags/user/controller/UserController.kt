package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "user api", description = "사용자 관련 api")
@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "사용자 회원가입")
    @PostMapping("/auth/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Unit> {
        userService.register(request.email, request.password)
        userService.sendCodeToEmail(request.email)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "회원가입 이메일 인증")
    @PostMapping("/auth/verify-email")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest): ResponseEntity<Unit> {
        userService.verifyEmail(request.email, request.verificationCode)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "로그인")
    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val (_, accessToken, refreshToken) = userService.login(request.email, request.password)
        return ResponseEntity.ok(LoginResponse(accessToken, refreshToken))
    }

    @Operation(summary = "소셜 로그인")
    @PostMapping("/auth/login/{socialType}")
    fun loginSocial(
        @RequestHeader(name = "Authorization") token: String,
        @PathVariable("socialType") socialType: String
    ): ResponseEntity<LoginResponse> {
        // val (_, accessToken, refreshToken) = userService.loginSocial(token, socialType)
        return ResponseEntity.ok(LoginResponse("", ""))
    }

    @Operation(summary = "비밀번호 찾기 이메일 인증")
    @PostMapping("/auth/forgot-password")
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest): ResponseEntity<Unit> {
        userService.sendCodeToEmail(request.email)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "비밀번호 초기화")
    @PostMapping("/auth/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<Unit> {
        userService.resetPassword(request.email, request.verificationCode, request.password)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "토큰 재발급")
    @PostMapping("/auth/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): RefreshTokenResponse {
        val refreshToken = request.refreshToken
        return userService.refreshToken(refreshToken)
    }

    @Operation(summary = "현재 로그인한 유저 조회")
    @GetMapping("/auth/me")
    fun me(
        @AuthUser user: User
    ): ResponseEntity<User> {
        println("authme start!!!!")
        return ResponseEntity.ok(user)
    }
}

data class RegisterRequest(
    val email: String,
    val password: String
)

data class VerifyEmailRequest(
    val email: String,
    val verificationCode: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)

data class ForgotPasswordRequest(
    val email: String
)

data class ResetPasswordRequest(
    val email: String,
    val verificationCode: String,
    val password: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
