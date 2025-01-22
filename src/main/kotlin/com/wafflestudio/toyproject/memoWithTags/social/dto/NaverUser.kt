package com.wafflestudio.toyproject.memoWithTags.social.dto

data class NaverOAuthToken(
    val token_type: String,
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String
)

data class NaverProfileResponse(
    val resultcode: String,
    val message: String,
    val response: NaverProfile
)

data class NaverProfile(
    val id: String,
    val nickname: String,
    val email: String
)
