package com.wafflestudio.toyproject.memoWithTags.mail.controller

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ApiErrorCodeExamples
import com.wafflestudio.toyproject.memoWithTags.mail.docs.SendEmailExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.mail.docs.VerifyEmailExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.mail.dto.MailRequest.SendMailRequest
import com.wafflestudio.toyproject.memoWithTags.mail.dto.MailRequest.VerifyMailRequest
import com.wafflestudio.toyproject.memoWithTags.mail.service.MailVerifService
import com.wafflestudio.toyproject.memoWithTags.mail.service.SendMailType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Tag(name = "mail api", description = "메일 관련 api")
@RestController
@RequestMapping("/api/v1")
class MailController(
    private val mailVerifService: MailVerifService
) {
    @Operation(summary = "메일 인증 요청")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(SendEmailExceptionDocs::class)
    @PostMapping("/mail")
    fun sendMail(
        @RequestBody request: SendMailRequest,
        @RequestParam type: SendMailType
    ): ResponseEntity<Unit> {
        mailVerifService.sendCodeToMail(request.email, type)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(summary = "메일 인증 확인")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(VerifyEmailExceptionDocs::class)
    @PostMapping("/mail/verify")
    fun verifyMail(
        @RequestBody request: VerifyMailRequest,
        @RequestParam type: SendMailType
    ): ResponseEntity<Unit> {
        mailVerifService.verifyMail(request.email, request.verificationCode, type)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
