package com.wafflestudio.toyproject.memoWithTags.user.service

import jakarta.mail.MessagingException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    fun sendEmail(toEmail: String, title: String, content: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(toEmail)
        helper.setSubject(title)
        helper.setText(content, true)
        helper.setReplyTo("memowithtags@gmail.com")
        try {
            mailSender.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw RuntimeException("unable to send email", e)
        }
    }
}
