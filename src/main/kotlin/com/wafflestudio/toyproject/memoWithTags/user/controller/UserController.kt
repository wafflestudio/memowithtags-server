package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ApiErrorCodeExamples
import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.LoginExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.MeExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.RefreshTokenExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.RegisterExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.ResetPasswordExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.SendEmailExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.UpdateNicknameExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.UpdatePasswordExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.VerifyEmailExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.user.WithdrawalExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserRequest.ForgotPasswordRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserRequest.LoginRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserRequest.RefreshTokenRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserRequest.RegisterRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserRequest.UpdateNicknameRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserRequest.UpdatePasswordRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserRequest.VerifyEmailRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserResponse.LoginResponse
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserResponse.RefreshTokenResponse
import com.wafflestudio.toyproject.memoWithTags.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "user api", description = "사용자 관련 api")
@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
) {

    @ApiErrorCodeExamples(RegisterExceptionDocs::class)
    @Operation(summary = "사용자 회원가입")
    @PostMapping("/auth/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<LoginResponse> {
        userService.register(request.email, request.password, request.nickname)
        val (_, accessToken, refreshToken) = userService.login(request.email, request.password)
        return ResponseEntity.status(HttpStatus.CREATED).body(LoginResponse(accessToken, refreshToken))
    }

    @Operation(summary = "메일 인증 요청")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(SendEmailExceptionDocs::class)
    @PostMapping("/auth/send-email")
    fun sendEmail(@RequestBody request: SendEmailRequest): ResponseEntity<Unit> {
        userService.sendCodeToEmail(request.email)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(summary = "메일 인증 확인")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(VerifyEmailExceptionDocs::class)
    @PostMapping("/auth/verify-email")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest): ResponseEntity<Unit> {
        userService.verifyEmail(request.email, request.verificationCode)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @ApiErrorCodeExamples(LoginExceptionDocs::class)
    @Operation(summary = "로그인")
    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val (_, accessToken, refreshToken) = userService.login(request.email, request.password)
        return ResponseEntity.ok(LoginResponse(accessToken, refreshToken))
    }

    @Operation(summary = "비밀번호 초기화")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(ResetPasswordExceptionDocs::class)
    @PostMapping("/auth/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<Unit> {
        userService.resetPasswordWithEmailVerification(request.email, request.password)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @ApiErrorCodeExamples(UpdatePasswordExceptionDocs::class)
    @Operation(summary = "비밀번호 수정(로그인 상태에서)")
    @PutMapping("/auth/password")
    fun updatePassword(
        @AuthUser user: User,
        @RequestBody request: UpdatePasswordRequest
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updatePassword(user, request.originalPassword, request.newPassword))
    }

    @ApiErrorCodeExamples(UpdateNicknameExceptionDocs::class)
    @Operation(summary = "닉네임 수정")
    @PutMapping("/auth/nickname")
    fun updateNickname(
        @AuthUser user: User,
        @RequestBody request: UpdateNicknameRequest
    ): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updateNickname(user, request.nickname))
    }

    @ApiErrorCodeExamples(RefreshTokenExceptionDocs::class)
    @Operation(summary = "토큰 재발급")
    @PostMapping("/auth/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): RefreshTokenResponse {
        val refreshToken = request.refreshToken
        return userService.refreshToken(refreshToken)
    }

    @Operation(summary = "회원 탈퇴(유저 삭제)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(WithdrawalExceptionDocs::class)
    @DeleteMapping("/auth/withdrawal")
    fun withdrawal(
        @AuthUser user: User,
        @RequestBody request: WithdrawalRequest
    ): ResponseEntity<Unit> {
        userService.deleteUser(user, request.email)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @ApiErrorCodeExamples(MeExceptionDocs::class)
    @Operation(summary = "현재 로그인한 유저 조회")
    @GetMapping("/auth/me")
    fun me(
        @AuthUser user: User
    ): ResponseEntity<User> {
        return ResponseEntity.ok(user)
    }
}

typealias WithdrawalRequest = ForgotPasswordRequest
typealias SendEmailRequest = ForgotPasswordRequest
typealias ResetPasswordRequest = LoginRequest
