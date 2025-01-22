package com.wafflestudio.toyproject.memoWithTags.social_login.dto

data class KakaoOAuthToken(
    val token_type: String,
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val refresh_token_expires_in: Int,
    val scope: String?
)

data class KakaoProfile(
    val id: Long,
    val connected_at: String,
    val properties: Properties,
    val kakao_account: KakaoAccount
)
data class Properties(
    val nickname: String
)
data class KakaoAccount(
    val profile_nickname_needs_agreement: Boolean,
    val profile: Profile,
    val has_email: Boolean,
    val email_needs_agreement: Boolean,
    val is_email_valid: Boolean,
    val is_email_verified: Boolean,
    val email: String
) {
    data class Profile(
        val nickname: String,
        val is_default_nickname: Boolean
    )
}
