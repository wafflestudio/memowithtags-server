package com.wafflestudio.toyproject.memoWithTags.social.controller

import com.wafflestudio.toyproject.memoWithTags.exception.OAuthRequestException
import com.wafflestudio.toyproject.memoWithTags.social.service.SocialLoginService
import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserResponse.LoginResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SocialLoginController(
    private val socialLoginService: SocialLoginService
) {
    @Operation(summary = "소셜 로그인 요청")
    @GetMapping("/auth/code/{provider}")
    fun oauthCallback(
        @RequestParam(value = "code", required = false) code: String?,
        @PathVariable provider: String
    ): ResponseEntity<Unit> {
        if (code == null) throw OAuthRequestException()
        val appLink = "memowithtags://oauth/$provider?code=$code"
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", appLink)
            .build()
    }

    @Operation(summary = "소셜 로그인 처리")
    @GetMapping("/auth/login/{provider}")
    fun oauthLogin(
        @RequestParam(value = "code") code: String,
        @PathVariable provider: String
    ): ResponseEntity<LoginResponse> {
        val socialType = SocialType.from(provider)
        val loginResult = when (socialType) {
            SocialType.KAKAO -> socialLoginService.kakaoLogin(code)
            SocialType.NAVER -> socialLoginService.naverLogin(code)
            SocialType.GOOGLE -> socialLoginService.googleLogin(code)
            else -> throw OAuthRequestException()
        }
        return ResponseEntity.ok(LoginResponse(loginResult.second, loginResult.third))
    }
}
