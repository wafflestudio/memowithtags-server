package com.wafflestudio.toyproject.memoWithTags.mail.dto

sealed class MailRequest {
    data class SendMailRequest(
        val email: String
    ) : MailRequest()

    data class VerifyMailRequest(
        val email: String,
        val verificationCode: String
    ) : MailRequest()
}