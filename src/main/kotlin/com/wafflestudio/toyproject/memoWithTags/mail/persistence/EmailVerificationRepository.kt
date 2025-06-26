package com.wafflestudio.toyproject.memoWithTags.mail.persistence

import org.springframework.data.repository.CrudRepository

interface EmailVerificationRepository : CrudRepository<EmailVerificationEntity, String> {
    fun deleteAllById(id: String)
}
