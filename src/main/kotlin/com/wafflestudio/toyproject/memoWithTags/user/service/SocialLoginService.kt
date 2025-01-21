package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.user.GoogleUtil
import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.KakaoUtil
import com.wafflestudio.toyproject.memoWithTags.user.NaverUtil
import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.user.dto.GoogleOAuthToken
import com.wafflestudio.toyproject.memoWithTags.user.dto.GoogleProfile
import com.wafflestudio.toyproject.memoWithTags.user.dto.KakaoOAuthToken
import com.wafflestudio.toyproject.memoWithTags.user.dto.KakaoProfile
import com.wafflestudio.toyproject.memoWithTags.user.dto.NaverOAuthToken
import com.wafflestudio.toyproject.memoWithTags.user.dto.NaverProfile
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SocialLoginService(
    private val userRepository: UserRepository,
    private val kakaoUtil: KakaoUtil,
    private val naverUtil: NaverUtil,
    private val googleUtil: GoogleUtil
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun naverCallback(accessCode: String): Triple<User, String, String> {
        val oAuthToken: NaverOAuthToken = naverUtil.requestToken(accessCode)
        val naverProfile: NaverProfile = naverUtil.requestProfile(oAuthToken)

        val naverEmail = naverProfile.email
        val userEntity = userRepository.findByEmail(naverEmail)
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.NAVER) {
            logger.info("user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else {
            logger.info("creating user $naverEmail")
            createNaverUser(naverProfile)
        }

        return Triple(
            user,
            JwtUtil.generateAccessToken(naverEmail),
            JwtUtil.generateRefreshToken(naverEmail)
        )
    }

    fun createNaverUser(naverProfile: NaverProfile): User {
        val naverEmail = naverProfile.email
        val naverNickname = naverProfile.nickname
        val encryptedPassword = "naver_registered_user"

        val userEntity = userRepository.save(
            UserEntity(
                email = naverEmail,
                nickname = naverNickname,
                hashedPassword = encryptedPassword,
                verified = true,
                socialType = SocialType.NAVER,
                createdAt = Instant.now()
            )
        )

        return User.fromEntity(userEntity)
    }

    fun kakaoCallBack(accessCode: String): Triple<User, String, String> {
        val oAuthToken: KakaoOAuthToken = kakaoUtil.requestToken(accessCode)
        val kakaoProfile: KakaoProfile = kakaoUtil.requestProfile(oAuthToken)

        val kakaoEmail = kakaoProfile.kakao_account.email
        val userEntity = userRepository.findByEmail(kakaoEmail)
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.KAKAO) {
            logger.info("user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else {
            logger.info("creating user $kakaoEmail")
            createKakaoUser(kakaoProfile)
        }

        return Triple(
            user,
            JwtUtil.generateAccessToken(kakaoEmail),
            JwtUtil.generateRefreshToken(kakaoEmail)
        )
    }

    fun createKakaoUser(kakaoProfile: KakaoProfile): User {
        val kakaoEmail = kakaoProfile.kakao_account.email
        val kakaoNickname = kakaoProfile.kakao_account.profile.nickname
        val encryptedPassword = "kakao_registered_user"

        val userEntity = userRepository.save(
            UserEntity(
                email = kakaoEmail,
                nickname = kakaoNickname,
                hashedPassword = encryptedPassword,
                verified = true,
                socialType = SocialType.KAKAO,
                createdAt = Instant.now()
            )
        )

        return User.fromEntity(userEntity)
    }

    fun googleCallback(accessCode: String): Triple<User, String, String> {
        val oAuthToken: GoogleOAuthToken = googleUtil.requestToken(accessCode)
        val googleProfile: GoogleProfile = googleUtil.requestProfile(oAuthToken)

        val googleEmail = googleProfile.email
        val userEntity = userRepository.findByEmail(googleEmail)
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.GOOGLE) {
            logger.info("google user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else {
            logger.info("creating google user $googleEmail")
            createGoogleUser(googleProfile)
        }

        return Triple(
            user,
            JwtUtil.generateAccessToken(googleEmail),
            JwtUtil.generateRefreshToken(googleEmail)
        )
    }

    fun createGoogleUser(profile: GoogleProfile): User {
        val googleEmail = profile.email
        val googleNickname = profile.name
        val encryptedPassword = "google_registered_user"

        val userEntity = userRepository.save(
            UserEntity(
                email = googleEmail,
                nickname = googleNickname,
                hashedPassword = encryptedPassword,
                verified = true,
                socialType = SocialType.GOOGLE,
                createdAt = Instant.now()
            )
        )

        return User.fromEntity(userEntity)
    }
}
