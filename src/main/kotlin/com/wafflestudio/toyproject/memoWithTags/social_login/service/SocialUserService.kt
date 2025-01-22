package com.wafflestudio.toyproject.memoWithTags.social_login.service

import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.GoogleProfile
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.KakaoProfile
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.NaverProfile
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import java.time.Instant
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SocialUserService(
    private val userRepository: UserRepository,
) {
    /**
     * 네이버 로그인 시 저장되어 있는 User 정보가 없을 경우, DB에 User를 생성하는 함수
     */
    @Transactional
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

    /**
     * 카카오 로그인 시 저장되어 있는 User 정보가 없을 경우, DB에 User를 생성하는 함수
     */
    @Transactional
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

    /**
     * 구글 로그인 시 저장되어 있는 User 정보가 없을 경우, DB에 User를 생성하는 함수
     */
    @Transactional
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

    /**
     * 해당 메일의 User를 찾는 함수. 없으면 null을 반환한다.
     */
    @Transactional
    fun findUserByEmail(email: String): UserEntity? {
        return userRepository.findByEmail(email)
    }
}