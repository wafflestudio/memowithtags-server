package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.user.dto.UserResponse.LoginResponse
import com.wafflestudio.toyproject.memoWithTags.user.service.SocialLoginService
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
    fun naverCallback() {
    }

    @GetMapping("/oauth/kakao")
    fun kakaoCallback(
        @RequestParam("code") code: String
    ): ResponseEntity<LoginResponse> {
        val (_, accessToken, refreshToken) = socialLoginService.kakaoCallBack(code)
        return ResponseEntity.ok(LoginResponse(accessToken, refreshToken))
    }

    @GetMapping("/oauth/google")
    fun googleCallback() {
    }
}
