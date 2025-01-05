package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.user.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.user.EmailNotFoundException
import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.contoller.User
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun register(
        email: String,
        password: String
    ): Boolean {
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = userRepository.save(
            UserEntity(
                email = email,
                hashedPassword = encryptedPassword
            )
        )
        // TODO: email 발송
        return true
    }

    fun verifyEmail(
        verificationCode: String
    ): Boolean {
        // TODO: 인증 코드 대조
        return true
    }

    fun login(
        email: String,
        password: String
    ): User {
        val user = userRepository.findByEmail(email) ?: throw EmailNotFoundException()
        return User.fromEntity(user)
    }

    fun authenticate(token: String): User {
        val userEmail = JwtUtil.validateAccessTokenGetUserId(token) ?: throw AuthenticationFailedException()
        val user = userRepository.findByEmail(userEmail) ?: throw AuthenticationFailedException()
        return User.fromEntity(user)
    }
}
