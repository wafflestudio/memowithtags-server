package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.exception.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.exception.EmailAlreadyExistsException
import com.wafflestudio.toyproject.memoWithTags.exception.EmailSendingException
import com.wafflestudio.toyproject.memoWithTags.exception.InValidTokenException
import com.wafflestudio.toyproject.memoWithTags.exception.MailVerificationException
import com.wafflestudio.toyproject.memoWithTags.exception.SignInInvalidException
import com.wafflestudio.toyproject.memoWithTags.exception.UserNotFoundException
import com.wafflestudio.toyproject.memoWithTags.mail.EmailVerification
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationEntity
import com.wafflestudio.toyproject.memoWithTags.mail.persistence.EmailVerificationRepository
import com.wafflestudio.toyproject.memoWithTags.mail.service.MailService
import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserResponse.RefreshTokenResponse
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val mailService: MailService
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    /**
     * 자체 로그인 과정 중 회원가입을 구현한 함수
     */
    @Transactional
    fun register(
        email: String,
        password: String,
        nickname: String
    ): User {
        if (userRepository.findByEmail(email) != null) throw EmailAlreadyExistsException()
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        // 메일 인증이 이루어지기 전까지 User의 verified 필드는 false이다.
        val userEntity = userRepository.save(
            UserEntity(
                email = email,
                nickname = nickname,
                hashedPassword = encryptedPassword,
                createdAt = Instant.now()
            )
        )
        logger.info("User registered: ${userEntity.id}, ${userEntity.email}")
        return User.fromEntity(userEntity)
    }

    /**
     * 회원가입 또는 비밀번호 변경 요청 후 인증용 메일을 발송하는 함수
     */
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
            logger.info("code: $verification")
            mailService.sendMail(email, title, content)
        } catch (e: Exception) {
            e.printStackTrace()
            throw EmailSendingException()
        }
    }

    /**
     * 인증 메일에 포함될 인증 코드를 랜덤으로 생성하는 함수. 6자리 숫자를 생성한다.
     */
    private fun createVerificationCode(email: String): EmailVerification {
        val randomCode: String = (100000..999999).random().toString()
        val codeEntity = EmailVerificationEntity(
            email = email,
            code = randomCode,
            expiryTime = LocalDateTime.now().plusDays(1)
        )
        return EmailVerification.fromEntity(emailVerificationRepository.save(codeEntity))
    }

    /**
     * 메일로 보내진 인증 번호와 유저가 입력한 인증 번호가 일치하는지 검증하는 함수
     */
    @Transactional
    fun verifyEmail(
        email: String,
        code: String
    ): Boolean {
        val verification = emailVerificationRepository.findByEmailAndCode(email, code) ?: throw MailVerificationException()
        if (verification.expiryTime.isBefore(LocalDateTime.now())) throw AuthenticationFailedException()
        val userEntity = userRepository.findByEmail(verification.email)
        // 인증 성공 시, user의 Verified 필드가 true로 바뀌어 정식 회원이 된다.
        userEntity!!.verified = true
        return true
    }

    /**
     * 회원가입된 유저의 자체 로그인 로직을 수행하는 함수
     */
    @Transactional
    fun login(
        email: String,
        password: String
    ): Triple<User, String, String> {
        val userEntity = userRepository.findByEmail(email) ?: throw SignInInvalidException()
        if (!BCrypt.checkpw(password, userEntity.hashedPassword)) throw SignInInvalidException()
        logger.info("User logged in: ${userEntity.id}, ${userEntity.email}")
        return Triple(
            User.fromEntity(userEntity),
            JwtUtil.generateAccessToken(userEntity.email),
            JwtUtil.generateRefreshToken(userEntity.email)
        )
    }

    /**
     * 비밀번호 변경을 위해 보내진 메일 인증을 완료하고, 비밀번호를 변경하는 함수
     */
    @Transactional
    fun resetPasswordWithEmailVerification(
        email: String,
        code: String,
        newPassword: String
    ) {
        if (verifyEmail(email, code)) {
            val userEntity = userRepository.findByEmail(email) ?: throw UserNotFoundException()
            userEntity.hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        }
    }

    /**
     * 유저 토큰을 받아 유저 정보를 반환하는 함수
     */
    @Transactional
    fun authenticate(
        accessToken: String
    ): User {
        if (!JwtUtil.isValidToken(accessToken)) throw AuthenticationFailedException()
        val email = JwtUtil.extractUserEmail(accessToken) ?: throw AuthenticationFailedException()
        val userEntity = userRepository.findByEmail(email) ?: throw AuthenticationFailedException()
        logger.info("User authenticated: ${userEntity.id}, ${userEntity.email}")
        return User.fromEntity(userEntity)
    }

    /**
     * 로그인 한 유저의 비밀번호를 변경하는 함수
     */
    @Transactional
    fun updatePassword(
        user: User,
        originalPassword: String,
        newPassword: String
    ): User {
        val userEntity = userRepository.findByEmail(user.email) ?: throw UserNotFoundException()
        if (!BCrypt.checkpw(originalPassword, userEntity.hashedPassword)) throw SignInInvalidException()
        userEntity.hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        return User.fromEntity(userRepository.save(userEntity))
    }

    /**
     * 로그인 한 유저의 닉네임을 변경하는 함수
     */
    @Transactional
    fun updateNickname(
        user: User,
        newNickname: String
    ): User {
        val userEntity = userRepository.findByEmail(user.email) ?: throw UserNotFoundException()
        userEntity.nickname = newNickname
        return User.fromEntity(userRepository.save(userEntity))
    }

    /**
     * accessToken 만료 시 refreshToken을 통해 유저를 확인하고 새로운 accessToken을 발급하는 함수
     */
    fun refreshToken(refreshToken: String): RefreshTokenResponse {
        if (!JwtUtil.isValidToken(refreshToken)) {
            throw InValidTokenException()
        }
        logger.info("Refreshing token: $refreshToken 1")
        val userEmail = JwtUtil.extractUserEmail(refreshToken) ?: throw InValidTokenException()
        logger.info("Refreshing token: $refreshToken 2")
        userRepository.findByEmail(userEmail) ?: throw UserNotFoundException()
        logger.info("Refreshing token: $refreshToken 3")
        val newAccessToken = JwtUtil.generateAccessToken(userEmail)
        logger.info("Refreshing token: $refreshToken, new Access token: $newAccessToken 4")
        return RefreshTokenResponse(
            accessToken = newAccessToken,
            refreshToken = refreshToken,
            expiresIn = JwtUtil.getAccessTokenExpiration()
        )
    }

    /**
     * 해당 메일의 User를 찾는 함수. 없으면 예외를 발생시킨다.
     */
    @Transactional
    fun getUserEntityByEmail(email: String): UserEntity {
        return userRepository.findByEmail(email) ?: throw UserNotFoundException()
    }

    /**
     * 메일 정오에 만료된 인증 코드 엔티티를 삭제하는 함수
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul") // 매일 정오에 만료 코드 삭제
    fun deleteExpiredVerificationCode() {
        emailVerificationRepository.deleteByExpiryTimeBefore(LocalDateTime.now())
    }

    /**
     * 매일 정오에 메일 인증이 되지 않은 유저를 삭제하는 함수
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul") // 매일 정오에 미인증 사용자 삭제
    fun deleteUnverifiedUser() {
        userRepository.deleteByVerified(false)
    }
}
