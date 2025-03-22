package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.EmailAlreadyExistsException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.EmailNotMatchException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.EmailNotValidException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.EmailNotVerifiedException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.EmailSendingException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.InValidTokenException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.MailVerificationException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.NicknameNotValidException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.PasswordNotValidException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.SignInInvalidException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.UpdatePasswordInvalidException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.UserNotFoundException
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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

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
        // 이메일 형식이 올바른지 검증한다.
        if (!email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}\$"))) throw EmailNotValidException()

        // 암호 형식이 올바른지 검증한다. (8자 이상 16자 이하, 영문 대소문자, 숫자, 특수문자 포함)
        if (!password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,16}\$"))) throw PasswordNotValidException()

        // 닉네임 형식이 올바른지 검증한다. (1자 이상 8자 이하)
        if (nickname.length !in 1..8) throw NicknameNotValidException()

        // 소셜 로그인 사용 여부와 무관하게 동일 이메일이 존재하기만 하면 예외 처리한다.
        if (userRepository.findByEmail(email) != null) throw EmailAlreadyExistsException()

        logger.info(emailVerificationRepository.findAll().toString())

        // 메일 인증 과정을 거치지 않고 바로 회원가입 시도 시 예외 처리한다. 검증 후 인증 데이터는 삭제한다.
        val verification = emailVerificationRepository.findByIdOrNull(email) ?: throw EmailNotVerifiedException()
        if (!verification.verified) throw EmailNotVerifiedException()
        emailVerificationRepository.deleteById(email)

        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        // 클라이언트에서 쓸 유저 식별 번호인 userNumber는 해당 유저가 서비스에 가입한 순서 + 1로 한다.
        val userNumber = userRepository.getMaxUserNumber() + 1

        val userEntity = userRepository.save(
            UserEntity(
                userNumber = userNumber,
                email = email,
                nickname = nickname,
                hashedPassword = encryptedPassword,
                createdAt = Instant.now()
            )
        )
        val user = User.fromEntity(userEntity)
        logger.info("User registered: $user")
        return user
    }

    /**
     * 회원가입 또는 비밀번호 변경 요청 후 인증용 메일을 발송하는 함수
     */
    @Transactional
    fun sendCodeToEmail(
        email: String
    ) {
        // 이미 인증 메일을 보낸 주소로 또 시도하는 경우에는 해당 이메일로 발송된 인증번호 데이터를 삭제한다.
        emailVerificationRepository.deleteAllById(email)

        val verification: EmailVerification = mailService.createVerificationCode(email)
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
     * 메일로 보내진 인증 번호와 유저가 입력한 인증 번호가 일치하는지 검증하는 함수
     */
    @Transactional
    fun verifyEmail(
        email: String,
        code: String
    ): Boolean {
        logger.info(emailVerificationRepository.findAll().toString())
        val verification = emailVerificationRepository.findByIdOrNull(email) ?: throw MailVerificationException()
        if (verification.code != code) throw MailVerificationException()
        // 인증 성공 시, verification의 Verified 필드가 true로 바뀌어 회원가입의 검증 절차를 통과한다.
        emailVerificationRepository.save(EmailVerificationEntity(email, code, true))
        logger.info("verified email code")
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
        if (userEntity.socialType != null) throw SignInInvalidException()
        if (!BCrypt.checkpw(password, userEntity.hashedPassword)) throw SignInInvalidException()
        val user = User.fromEntity(userEntity)
        logger.info("User logged in: $user")
        return Triple(
            user,
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
        newPassword: String
    ) {
        // 인증된 이메일인지 확인하고, 검증 후 인증 데이터를 삭제한다.
        val verification = emailVerificationRepository.findByIdOrNull(email) ?: throw EmailNotVerifiedException()
        if (!verification.verified) throw EmailNotVerifiedException()
        emailVerificationRepository.deleteById(email)

        // 해당하는 유저가 없으면 예외를 발생시킨다.
        val userEntity = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        // 비밀번호를 변경한다.
        userEntity.hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        logger.info("password reset: $email")
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
        if (!BCrypt.checkpw(originalPassword, userEntity.hashedPassword)) throw UpdatePasswordInvalidException()
        userEntity.hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())
        logger.info("password updated: $user")
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
        val userEmail = JwtUtil.extractUserEmail(refreshToken) ?: throw InValidTokenException()
        userRepository.findByEmail(userEmail) ?: throw UserNotFoundException()
        val newAccessToken = JwtUtil.generateAccessToken(userEmail)
        logger.info("Refreshing token: $refreshToken, new Access token: $newAccessToken 4")
        return RefreshTokenResponse(
            accessToken = newAccessToken,
            refreshToken = refreshToken,
            expiresIn = JwtUtil.getAccessTokenExpiration()
        )
    }

    /**
     * 해당 유저를 레포지토리에서 지우는 함수
     */
    @Transactional
    fun deleteUser(
        user: User,
        email: String
    ) {
        if (user.email != email) throw EmailNotMatchException()
        userRepository.deleteById(user.id)
        logger.info("User deleted: $user")
    }

    /**
     * 해당 메일의 User를 찾는 함수. 없으면 예외를 발생시킨다.
     */
    @Transactional
    fun getUserEntityByEmail(email: String): UserEntity {
        return userRepository.findByEmail(email) ?: throw UserNotFoundException()
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
