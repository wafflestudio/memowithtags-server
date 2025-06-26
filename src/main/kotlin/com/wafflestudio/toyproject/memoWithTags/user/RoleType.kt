package com.wafflestudio.toyproject.memoWithTags.user

enum class RoleType(val type: String) {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    companion object {
        fun from(type: String?): RoleType? = entries.find { it.type == type }
    }
}
