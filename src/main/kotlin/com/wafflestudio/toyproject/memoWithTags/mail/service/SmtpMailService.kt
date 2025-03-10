package com.wafflestudio.toyproject.memoWithTags.mail.service

import com.wafflestudio.toyproject.memoWithTags.mail.EmailVerification
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationEntity
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationRepository
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.context.annotation.Profile
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Profile("prod")
@Service
class SmtpMailService(
    private val mailSender: JavaMailSender,
    private val emailVerificationRepository: EmailVerificationRepository
) : MailService {
    override fun sendMail(toEmail: String, title: String, content: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        try {
            helper.apply {
                setFrom("memowithtags@gmail.com")
                setTo(toEmail)
                setSubject(title)
                setText(content, true)
                setReplyTo("memowithtags@gmail.com")
            }
            mailSender.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw RuntimeException("unable to send email", e)
        }
    }

    /**
     * 인증 메일에 포함될 인증 코드를 랜덤으로 생성하는 함수. 6자리 숫자를 생성한다.
     */
    @Transactional
    override fun createVerificationCode(email: String): EmailVerification {
        val randomCode: String = if (email == "test@example.com") {
            "000000"
        } else {
            (100000..999999).random().toString()
        }

        val codeEntity = EmailVerificationEntity(
            id = email,
            code = randomCode,
            verified = false
        )
        return EmailVerification.fromEntity(emailVerificationRepository.save(codeEntity))
    }
}
