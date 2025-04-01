package com.wafflestudio.toyproject.memoWithTags.mail.service

import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.EmailSendingException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.MailVerificationException
import com.wafflestudio.toyproject.memoWithTags.mail.EmailVerification
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationEntity
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MailVerifService(
    private val emailVerificationRepository: EmailVerificationRepository,
    private val mailService: MailService
) {
    /**
     * 회원가입 또는 비밀번호 재설정 등 목적에 따른 인증용 메일을 발송하는 함수
     */
    @Transactional
    fun sendCodeToMail(
        email: String,
        purpose: SendMailType
    ) {
        // 이미 인증 메일을 보낸 주소로 또 시도하는 경우에는 해당 이메일로 발송된 인증번호 데이터를 삭제한다.
        emailVerificationRepository.deleteAllById(email)

        val purposeAndEmail = "${purpose.name},$email"
        val hangulPurpose = when (purpose) {
            SendMailType.Register -> "회원가입"
            SendMailType.ResetPassword -> "비밀번호 재설정"
        }
        val verification: EmailVerification = mailService.createVerificationCode(purposeAndEmail)
        val verifCode = verification.code
        val title = "[Memo with tags] 인증번호 [$verifCode] 를 입력해주세요"
        val content: String = "<html>" +
            "<body>" +
            "<h1>인증번호 안내</h1>" +
            "<p>안녕하세요, Memo with tags 팀입니다.</p>" +
            "<b>[$hangulPurpose]을 위해, 아래의 인증번호 6자리를 진행 중인 화면에 입력하여 5분 내에 인증을 완료해주세요.</b>" +
            "<h2>인증번호<br>$verifCode</h2>" +
            "<p>인증번호는 이메일 발송 시점으로부터 3분 동안 유효합니다.</p>" +
            "<footer style='color: grey; font-size: small;'>" +
            "<p>본 메일은 자동응답 메일이므로 본 메일에 회신하지 마시기 바랍니다.</p>" +
            "</footer>" +
            "</body>" +
            "</html>"
        try {
            mailService.sendMail(email, title, content)
        } catch (e: Exception) {
            e.printStackTrace()
            throw EmailSendingException()
        }
    }

    /**
     * 특정 목적에 의해 메일로 보내진 인증 번호와 유저가 입력한 인증 번호가 일치하는지 검증하는 함수
     */
    @Transactional
    fun verifyMail(
        email: String,
        code: String,
        purpose: SendMailType
    ): Boolean {
        val purposeAndEmail = "${purpose.name},$email"
        val verification = emailVerificationRepository.findByIdOrNull(purposeAndEmail) ?: throw MailVerificationException()
        if (verification.code != code) throw MailVerificationException()
        // 인증 성공 시, verification의 Verified 필드가 true로 바뀌어 회원가입의 검증 절차를 통과한다.
        emailVerificationRepository.save(EmailVerificationEntity(email, code, true))
        return true
    }
}
