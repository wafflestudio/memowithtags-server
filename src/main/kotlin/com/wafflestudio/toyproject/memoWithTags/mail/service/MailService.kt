package com.wafflestudio.toyproject.memoWithTags.mail.service

import com.wafflestudio.toyproject.memoWithTags.mail.EmailVerification

interface MailService {
    /**
     * 메일을 보내는 함수.
     * 개발 환경에서는 로그만 출력하고, 배포 환경에서만 실제 메일을 보내기 위해 인터페이스를 선언함
     */
    fun sendMail(toEmail: String, title: String, content: String)

    fun createVerificationCode(email: String, purpose: SendMailType): EmailVerification
}
