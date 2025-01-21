package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.exception.OAuthRequestException
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserResponse.LoginResponse
import com.wafflestudio.toyproject.memoWithTags.user.service.SocialLoginService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SocialLoginController(
    private val socialLoginService: SocialLoginService
) {

    @GetMapping("/oauth/naver")
    fun naverCallback(
        @RequestParam(value = "code", required = false) code: String?,
        @RequestParam(value = "state", required = false) state: String?,
        @RequestParam(value = "error", required = false) error: String?,
        @RequestParam(value = "error_description", required = false) errorDescription: String?
    ): ResponseEntity<LoginResponse> {
        if (code == null || error != null) {
            throw OAuthRequestException()
        }
        val (_, accessToken, refreshToken) = socialLoginService.naverCallback(code)
        return ResponseEntity.ok(LoginResponse(accessToken, refreshToken))
    }

    @GetMapping("/oauth/kakao")
    fun kakaoCallback(
        @RequestParam("code") code: String
    ): ResponseEntity<Unit> {
        val appLink = "memowithtags://oauth/kakao?code=$code"
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", appLink)
            .build()
    }

    @GetMapping("/oauth/kakao/login")
    fun kakaoLogin(
        @RequestParam("code") code: String
    ): ResponseEntity<LoginResponse> {
        val (_, accessToken, refreshToken) = socialLoginService.kakaoCallBack(code)
        return ResponseEntity.ok(LoginResponse(accessToken, refreshToken))
    }

    @GetMapping("/oauth/google")
    fun googleCallback(
        @RequestParam("code") code: String
    ): ResponseEntity<LoginResponse> {
        val (_, accessToken, refreshToken) = socialLoginService.googleCallback(code)
        return ResponseEntity.ok(LoginResponse(accessToken, refreshToken))
    }
}
