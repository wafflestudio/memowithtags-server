package com.wafflestudio.toyproject.memoWithTags.mail.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface EmailVerificationRepository : JpaRepository<EmailVerificationEntity, Long> {
    fun deleteByExpiryTimeBefore(expiryTime: LocalDateTime)
    fun findByEmailAndCode(email: String, code: String): EmailVerificationEntity?
}
