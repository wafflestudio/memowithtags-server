package com.wafflestudio.toyproject.memoWithTags.user.service

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    private val fromMail: String
) {

    fun sendEmail(toEmail: String, title: String, content: String) {
        val message: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        try {
            helper.apply {
                setFrom(fromMail)
                setTo(toEmail)
                setSubject(title)
                setText(content, true)
            }
            mailSender.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw RuntimeException("unable to send email", e)
        }
    }
}
