package com.wafflestudio.toyproject.memoWithTags.mail.persistence

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "email_verification")
data class EmailVerificationEntity(
    @Id
    val id: String, // type and email (type,xx@xx.xx)

    val code: String, // verification code (000000)

    var verified: Boolean, // default: false, verified user: true

    @TimeToLive
    var ttl: Long? = 300 // 메일 인증을 완료하지 않은 인증 정보는 TTL 5분으로 설정
) {
    fun verify() {
        this.verified = true
        this.ttl = 86400 // 메일 인증이 확인된 인증 정보는 TTL 24시간으로 설정
    }
}
