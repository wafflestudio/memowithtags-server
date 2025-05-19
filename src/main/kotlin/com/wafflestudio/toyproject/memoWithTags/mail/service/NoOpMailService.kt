package com.wafflestudio.toyproject.memoWithTags.mail.service

import com.wafflestudio.toyproject.memoWithTags.mail.EmailVerification
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationEntity
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Profile("local")
@Service
class NoOpMailService(
    val emailVerificationRepository: EmailVerificationRepository
) : MailService {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun sendMail(toEmail: String, title: String, content: String) {
        logger.info("Sending mail to $toEmail")
        logger.info("Title: $title")
        logger.info("Content: $content")
    }

    /**
     * 인증 메일에 포함될 인증 코드를 랜덤으로 생성하는 함수. 6자리 숫자를 생성한다.
     */
    @Transactional
    override fun createVerificationCode(email: String, purpose: SendMailType): EmailVerification {
        val randomCode = "000000"
        val codeEntity = EmailVerificationEntity(
            id = "${purpose.name},$email",
            code = randomCode,
            verified = false
        )
        return EmailVerification.fromEntity(emailVerificationRepository.save(codeEntity))
    }
}
