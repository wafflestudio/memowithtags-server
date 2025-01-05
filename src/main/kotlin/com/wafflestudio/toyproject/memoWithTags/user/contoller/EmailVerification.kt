package com.wafflestudio.toyproject.memoWithTags.user.contoller

import com.wafflestudio.toyproject.memoWithTags.user.persistence.EmailVerificationEntity
import java.time.LocalDateTime

class EmailVerification(
    val id: Long,
    val email: String,
    val code: String,
    val expiryTime: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: EmailVerificationEntity): EmailVerification {
            return EmailVerification(
                id = entity.id!!,
                email = entity.email,
                code = entity.code,
                expiryTime = entity.expiryTime
            )
        }
    }
}
