package com.wafflestudio.toyproject.memoWithTags.social_login.service

import com.wafflestudio.toyproject.memoWithTags.exception.EmailAlreadyExistsException
import com.wafflestudio.toyproject.memoWithTags.user.GoogleUtil
import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.KakaoUtil
import com.wafflestudio.toyproject.memoWithTags.user.NaverUtil
import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.GoogleOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.GoogleProfile
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.KakaoOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.KakaoProfile
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.NaverOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.NaverProfile
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SocialLoginService(
    private val socialUserService: SocialUserService,
    private val kakaoUtil: KakaoUtil,
    private val naverUtil: NaverUtil,
    private val googleUtil: GoogleUtil
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 네이버 프로필 정보를 받아온 후, 로그인 또는 회원가입 후 로그인 로직을 처리하는 함수
     */
    fun naverLogin(accessCode: String): Triple<User, String, String> {
        val oAuthToken: NaverOAuthToken = naverUtil.requestToken(accessCode)
        val naverProfile: NaverProfile = naverUtil.requestProfile(oAuthToken)

        val naverEmail = naverProfile.email
        val userEntity = socialUserService.findUserByEmail(naverEmail)

        // 기존에 등록된 이메일이 있으면서 다른 서비스로 로그인을 한 경우 예외 발생
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.NAVER) {
            logger.info("naver user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else if (userEntity == null) {
            logger.info("creating naver user $naverEmail")
            socialUserService.createNaverUser(naverProfile)
        } else throw EmailAlreadyExistsException()

        return Triple(
            user,
            JwtUtil.generateAccessToken(naverEmail),
            JwtUtil.generateRefreshToken(naverEmail)
        )
    }

    /**
     * 카카오 프로필 정보를 받아온 후, 로그인 또는 회원가입 후 로그인 로직을 처리하는 함수
     */
    fun kakaoLogin(accessCode: String): Triple<User, String, String> {
        val oAuthToken: KakaoOAuthToken = kakaoUtil.requestToken(accessCode)
        val kakaoProfile: KakaoProfile = kakaoUtil.requestProfile(oAuthToken)

        val kakaoEmail = kakaoProfile.kakao_account.email
        val userEntity = socialUserService.findUserByEmail(kakaoEmail)

        // 기존에 등록된 이메일이 있으면서 다른 서비스로 로그인을 한 경우 예외 발생
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.KAKAO) {
            logger.info("kakao user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else if (userEntity == null) {
            logger.info("creating kakao user $kakaoEmail")
            socialUserService.createKakaoUser(kakaoProfile)
        } else throw EmailAlreadyExistsException()

        return Triple(
            user,
            JwtUtil.generateAccessToken(kakaoEmail),
            JwtUtil.generateRefreshToken(kakaoEmail)
        )
    }

    /**
     * 구글 프로필 정보를 받아온 후, 로그인 또는 회원가입 후 로그인 로직을 처리하는 함수
     */
    fun googleLogin(accessCode: String): Triple<User, String, String> {
        val oAuthToken: GoogleOAuthToken = googleUtil.requestToken(accessCode)
        val googleProfile: GoogleProfile = googleUtil.requestProfile(oAuthToken)

        val googleEmail = googleProfile.email
        val userEntity = socialUserService.findUserByEmail(googleEmail)

        // 기존에 등록된 이메일이 있으면서 다른 서비스로 로그인을 한 경우 예외 발생
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.GOOGLE) {
            logger.info("google user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else if (userEntity == null) {
            logger.info("creating google user $googleEmail")
            socialUserService.createGoogleUser(googleProfile)
        } else throw EmailAlreadyExistsException()

        return Triple(
            user,
            JwtUtil.generateAccessToken(googleEmail),
            JwtUtil.generateRefreshToken(googleEmail)
        )
    }
}
