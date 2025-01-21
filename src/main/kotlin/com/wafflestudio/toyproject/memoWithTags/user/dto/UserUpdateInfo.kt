package com.wafflestudio.toyproject.memoWithTags.user.dto

import com.wafflestudio.toyproject.memoWithTags.user.RoleType
import com.wafflestudio.toyproject.memoWithTags.user.SocialType

class UserUpdateInfo(
    val nickname: String,
    val password: String,
    val verified: Boolean,
    val role: RoleType,
    val socialType: SocialType
)
