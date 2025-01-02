package com.wafflestudio.toyproject.memoWithTags.user.contoller

@RestController
class UserController(

) {
    @GetMapping("/api/v1/auth/register")
    fun register(@RequestBody request: RegisterRequest): RegisterResponse {

    }

    @PostMapping("/api/v1/auth/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {

    }

    @PostMapping("/api/v1/auth/logout")
    fun logout(@RequestBody request: LogoutRequest) {

    }

    @PostMapping("/api/v1/auth/verify-email")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest) {

    }

    @PostMapping("/api/v1/auth/forgot-password")
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest) {

    }

    @PostMapping("/api/v1/auth/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest) {

    }

    @PostMapping("/api/v1/auth/refresh-token")
    fun refreshToken(): RefreshTokenResponse {

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
