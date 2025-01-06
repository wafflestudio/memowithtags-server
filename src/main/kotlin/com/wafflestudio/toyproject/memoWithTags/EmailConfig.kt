package com.wafflestudio.toyproject.memoWithTags

import com.wafflestudio.toyproject.memoWithTags.user.service.EmailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

@Configuration
class EmailConfig(
    @Value("\${spring.mail.host}")
    private val host: String,

    @Value("\${spring.mail.port}")
    private val port: Int,
  
    @Value("kybs0627@snu.ac.kr")
    private val username: String,

    @Value("Dlsms27!8")
    private val password: String,

    @Value("\${spring.mail.properties.mail.smtp.auth}")
    private val auth: Boolean,

    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    private val starttlsEnable: Boolean,

    @Value("\${spring.mail.properties.mail.smtp.starttls.required}")
    private val starttlsRequired: Boolean,

    @Value("\${spring.mail.properties.mail.smtp.connectiontimeout}")
    private val connectionTimeout: Int,

    @Value("\${spring.mail.properties.mail.smtp.timeout}")
    private val timeout: Int,

    @Value("\${spring.mail.properties.mail.smtp.writetimeout}")
    private val writeTimeout: Int
) {
    @Bean
    fun emailService(): EmailService {
        return EmailService(javaMailSender())
    }

    @Bean
    fun javaMailSender(): JavaMailSender {
        return JavaMailSenderImpl().apply {
            host = this@EmailConfig.host
            port = this@EmailConfig.port
            username = this@EmailConfig.username
            password = this@EmailConfig.password
            defaultEncoding = "UTF-8"
            javaMailProperties.putAll(getMailProperties())
        }
    }

    private fun getMailProperties(): Properties {
        return Properties().apply {
            put("mail.smtp.auth", auth.toString())
            put("mail.smtp.starttls.enable", starttlsEnable.toString())
            put("mail.smtp.starttls.required", starttlsRequired.toString())
            put("mail.smtp.connectiontimeout", connectionTimeout.toString())
            put("mail.smtp.timeout", timeout.toString())
            put("mail.smtp.writetimeout", writeTimeout.toString())
        }
    }
}
