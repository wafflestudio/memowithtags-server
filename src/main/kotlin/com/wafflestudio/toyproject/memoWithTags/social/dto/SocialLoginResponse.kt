package com.wafflestudio.toyproject.memoWithTags.social.dto

data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean
)
