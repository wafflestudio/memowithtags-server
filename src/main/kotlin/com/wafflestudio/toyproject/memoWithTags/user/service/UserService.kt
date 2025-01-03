package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.user.*
import com.wafflestudio.toyproject.memoWithTags.user.contoller.UserDTO
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt

class UserService(
    private val userRepository: UserRepository
) {
    fun register(
        email: String,
        password: String,
    ): Boolean {
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val user = userRepository.save(
            UserEntity(
                email = email,
                hashedPassword = encryptedPassword,
            )
        )
        // TODO: email 발송
        return true
    }

    fun verifyEmail(
        verificationCode: String,
    ): Boolean {
        // TODO: 인증 코드 대조
        return true
    }

    fun login(
        email: String,
        password: String,
    ): UserDTO {
        val user = userRepository.findByEmail(email) ?: throw EmailNotFoundException()
        return UserDTO.fromEntity(user)
    }
}
