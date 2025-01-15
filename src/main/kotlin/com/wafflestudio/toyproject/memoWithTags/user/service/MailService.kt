package com.wafflestudio.toyproject.memoWithTags.user.service


interface MailService {
    fun sendMail(toEmail: String, title: String, content: String)
}