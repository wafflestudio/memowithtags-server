package com.wafflestudio.toyproject.memoWithTags.user

enum class SocialType(val value: String) {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    companion object {
        fun from(value: String?): SocialType? = entries.find { it.value == value }
    }
}