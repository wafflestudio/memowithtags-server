package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.KakaoUtil
import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.user.dto.KakaoOAuthToken
import com.wafflestudio.toyproject.memoWithTags.user.dto.KakaoProfile
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SocialLoginService(
    private val userRepository: UserRepository,
    private val kakaoUtil: KakaoUtil
) {
    private val logger = LoggerFactory.getLogger(javaClass)

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
}
