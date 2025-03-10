package com.wafflestudio.toyproject.memoWithTags.mail

import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationEntity

class EmailVerification(
    val email: String,
    val code: String
) {
    companion object {
        fun fromEntity(entity: EmailVerificationEntity): EmailVerification {
            return EmailVerification(
                email = entity.id,
                code = entity.code
            )
        }
    }

    override fun toString(): String {
        return "{email: $email, code: $code}"
    }
}
