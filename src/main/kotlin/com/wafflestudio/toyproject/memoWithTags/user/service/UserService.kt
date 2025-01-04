package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.user.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.user.EmailNotFoundException
import com.wafflestudio.toyproject.memoWithTags.user.EmailVerificationFailureException
import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.SignInInvalidPasswordException
import com.wafflestudio.toyproject.memoWithTags.user.contoller.EmailVerification
import com.wafflestudio.toyproject.memoWithTags.user.contoller.User
import com.wafflestudio.toyproject.memoWithTags.user.persistence.EmailVerificationEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.EmailVerificationRepository
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val emailService: EmailService
) {
    @Transactional
    fun register(
        email: String,
        password: String
    ): User {
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val userEntity = userRepository.save(
            UserEntity(
                email = email,
                hashedPassword = encryptedPassword,
                createdAt = Instant.now()
            )
        )
        return User.fromEntity(userEntity)
    }

    fun sendCodeToEmail(
        email: String
    ) {
        val verification: EmailVerification = createVerificationCode(email)
        val title = "Memo with tags 이메일 인증 번호"
        val content: String = "<html>" +
            "<body>" +
            "<h1>이메일 인증 코드: " + verification.code + "</h1>" +
            "앱으로 돌아가서 해당 코드를 입력하세요." +
            "<footer style='color: grey; font-size: small;'>" +
            "<p>본 메일은 자동응답 메일이므로 본 메일에 회신하지 마시기 바랍니다.</p>" +
            "</footer>" +
            "</body>" +
            "</html>"
        try {
            emailService.sendEmail(email, title, content)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to send verification code", e)
        }
    }

    private fun createVerificationCode(email: String): EmailVerification {
        val randomCode: String = (100000..999999).random().toString()
        val codeEntity = EmailVerificationEntity(
            email = email,
            code = randomCode,
            expiryTime = LocalDateTime.now().plusDays(1)
        )
        return EmailVerification.fromEntity(emailVerificationRepository.save(codeEntity))
    }

    @Transactional
    fun verifyEmail(
        email: String,
        code: String
    ): Boolean {
        val verification = emailVerificationRepository.findByEmailAndCode(email, code) ?: return false
        if (verification.expiryTime.isBefore(LocalDateTime.now())) throw EmailVerificationFailureException()
        val userEntity = userRepository.findByEmail(verification.email)
        userEntity!!.verified = true
        return true
    }

    @Transactional
    fun login(
        email: String,
        password: String
    ): Triple<User, String, String> {
        val userEntity = userRepository.findByEmail(email) ?: throw EmailNotFoundException()
        if (!BCrypt.checkpw(password, userEntity.hashedPassword)) throw SignInInvalidPasswordException()
        return Triple(
            User.fromEntity(userEntity),
            JwtUtil.generateAccessToken(userEntity.email),
            JwtUtil.generateRefreshToken(userEntity.email)
        )
    }

    @Transactional
    fun authenticate(
        accessToken: String,
    ): User {
        val email = JwtUtil.validateAccessTokenGetUserId(accessToken) ?: throw AuthenticationFailedException()
        val userEntity = userRepository.findByEmail(email) ?: throw AuthenticationFailedException()
        return User.fromEntity(userEntity)
    }

    @Transactional
    @Scheduled(cron = "0 0 12 * * ?") // 매일 정오에 만료 코드 삭제
    fun deleteExpiredVerificationCode() {
        emailVerificationRepository.deleteByExpiryTimeBefore(LocalDateTime.now())
    }

    @Transactional
    @Scheduled(cron = "0 0 12 * * ?") // 매일 정오에 미인증 사용자 삭제
    fun deleteUnverifiedUser() {
        userRepository.deleteByVerified(false)
    }
}
