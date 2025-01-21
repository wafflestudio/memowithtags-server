package com.wafflestudio.toyproject.memoWithTags.user.dto

data class GoogleOAuthToken(
    val access_token: String,
    val expires_in: Int,
    val scope: String,
    val token_type: String,
    val id_token: String
)

data class GoogleProfile(
    val id: String,
    val email: String,
    val verified_email: Boolean,
    val name: String,
    val given_name: String,
    val family_name: String,
    val picture: String?,
    val locale: String?
)