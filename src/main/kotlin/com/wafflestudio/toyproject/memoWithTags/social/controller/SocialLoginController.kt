package com.wafflestudio.toyproject.memoWithTags.social.controller

import com.wafflestudio.toyproject.memoWithTags.exception.OAuthRequestException
import com.wafflestudio.toyproject.memoWithTags.social.dto.SocialLoginResponse
import com.wafflestudio.toyproject.memoWithTags.social.service.SocialLoginService
import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserResponse.LoginResponse
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "소셜 로그인 요청")
    @GetMapping("/auth/code/{provider}")
    fun oauthCallback(
        @RequestParam(value = "code", required = false) code: String?,
        @PathVariable provider: String
    ): ResponseEntity<Unit> {
        if (code == null) throw OAuthRequestException()
        val appLink = "memowithtags://oauth/$provider?code=$code"

        logger.info("redirect request to $appLink")
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", appLink)
            .build()
    }

    @Operation(summary = "소셜 로그인 처리")
    @GetMapping("/auth/login/{provider}")
    fun oauthLogin(
        @RequestParam(value = "code") code: String,
        @PathVariable provider: String
    ): ResponseEntity<SocialLoginResponse> {
        val socialType = SocialType.from(provider)
        val socialLoginResponse = when (socialType) {
            SocialType.KAKAO -> socialLoginService.kakaoLogin(code)
            SocialType.NAVER -> socialLoginService.naverLogin(code)
            SocialType.GOOGLE -> socialLoginService.googleLogin(code)
            else -> throw OAuthRequestException()
        }

        logger.info("social login result: (${socialLoginResponse.accessToken}, ${socialLoginResponse.isNewUser})")
        return ResponseEntity.ok(socialLoginResponse)
    }
}
