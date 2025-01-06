package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.exception.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.exception.EmailAlreadyExistsException
import com.wafflestudio.toyproject.memoWithTags.exception.EmailNotFoundException
import com.wafflestudio.toyproject.memoWithTags.exception.InValidTokenException
import com.wafflestudio.toyproject.memoWithTags.exception.SignInInvalidPasswordException
import com.wafflestudio.toyproject.memoWithTags.user.CustomUserDetails
import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.contoller.User
import com.wafflestudio.toyproject.memoWithTags.user.controller.RefreshTokenResponse
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import java.time.Instant
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
): UserDetailsService {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    fun register(
        email: String,
        password: String
    ): User {
        if (userRepository.findByEmail(email) != null) throw EmailAlreadyExistsException()
        val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val userEntity = userRepository.save(
            UserEntity(
                email = email,
                hashedPassword = encryptedPassword,
                createdAt = Instant.now()
            )
        )
        logger.info("User registered: ${userEntity.id}, ${userEntity.email}")
        return User.fromEntity(userEntity)
    }

    @Transactional
    fun login(
        email: String,
        password: String
    ): Triple<User, String, String> {
        val userEntity = userRepository.findByEmail(email) ?: throw EmailNotFoundException()
        if (!BCrypt.checkpw(password, userEntity.hashedPassword)) throw SignInInvalidPasswordException()
        logger.info("User logged in: ${userEntity.id}, ${userEntity.email}")
        return Triple(
            User.fromEntity(userEntity),
            JwtUtil.generateAccessToken(userEntity.email),
            JwtUtil.generateRefreshToken(userEntity.email)
        )
    }

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

    fun refreshToken(refreshToken: String): RefreshTokenResponse {
        if (!JwtUtil.isValidToken(refreshToken)) {
            throw InValidTokenException()
        }
        val userEmail = JwtUtil.extractUserEmail(refreshToken) ?: throw InValidTokenException()
        userRepository.findByEmail(userEmail) ?: throw EmailNotFoundException()

        val newAccessToken = JwtUtil.generateAccessToken(userEmail)

        return RefreshTokenResponse(
            accessToken = newAccessToken,
            refreshToken = refreshToken,
            expiresIn = JwtUtil.getAccessTokenExpiration() / 1000
        )
    }

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw EmailNotFoundException()
        return CustomUserDetails(user)
    }
}
