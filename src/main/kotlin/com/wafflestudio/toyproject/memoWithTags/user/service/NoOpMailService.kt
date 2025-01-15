package com.wafflestudio.toyproject.memoWithTags.user.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Profile("!prod")
@Service
class NoOpMailService : MailService {
    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun sendMail(toEmail: String, title: String, content: String) {
        logger.info("Sending mail to $toEmail")
        logger.info("Title: $title")
        logger.info("Content: $content")
    }
}