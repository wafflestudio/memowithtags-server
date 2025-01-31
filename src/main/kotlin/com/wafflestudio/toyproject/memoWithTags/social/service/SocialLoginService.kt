package com.wafflestudio.toyproject.memoWithTags.social.service

import com.wafflestudio.toyproject.memoWithTags.exception.EmailAlreadyExistsException
import com.wafflestudio.toyproject.memoWithTags.social.GoogleUtil
import com.wafflestudio.toyproject.memoWithTags.social.KakaoUtil
import com.wafflestudio.toyproject.memoWithTags.social.NaverUtil
import com.wafflestudio.toyproject.memoWithTags.social.dto.GoogleOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social.dto.GoogleProfile
import com.wafflestudio.toyproject.memoWithTags.social.dto.KakaoOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social.dto.KakaoProfile
import com.wafflestudio.toyproject.memoWithTags.social.dto.NaverOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social.dto.NaverProfile
import com.wafflestudio.toyproject.memoWithTags.social.dto.SocialLoginResponse
import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.SocialType
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
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
    fun naverLogin(accessCode: String): SocialLoginResponse {
        val oAuthToken: NaverOAuthToken = naverUtil.requestToken(accessCode)
        val naverProfile: NaverProfile = naverUtil.requestProfile(oAuthToken)
        var isNewUser = false

        val naverEmail = naverProfile.email
        val userEntity = socialUserService.findUserByEmail(naverEmail)

        // 기존에 등록된 이메일이 있으면서 다른 서비스로 로그인을 한 경우 예외 발생
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.NAVER) {
            logger.info("naver user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else if (userEntity == null) {
            logger.info("creating naver user $naverEmail")
            isNewUser = true
            socialUserService.createNaverUser(naverProfile)
        } else {
            throw EmailAlreadyExistsException()
        }

        return SocialLoginResponse(
            accessToken = JwtUtil.generateAccessToken(naverEmail),
            refreshToken = JwtUtil.generateRefreshToken(naverEmail),
            isNewUser = isNewUser
        )
    }

    /**
     * 카카오 프로필 정보를 받아온 후, 로그인 또는 회원가입 후 로그인 로직을 처리하는 함수
     */
    fun kakaoLogin(accessCode: String): SocialLoginResponse {
        val oAuthToken: KakaoOAuthToken = kakaoUtil.requestToken(accessCode)
        val kakaoProfile: KakaoProfile = kakaoUtil.requestProfile(oAuthToken)
        var isNewUser = false

        val kakaoEmail = kakaoProfile.kakao_account.email
        val userEntity = socialUserService.findUserByEmail(kakaoEmail)

        // 기존에 등록된 이메일이 있으면서 다른 서비스로 로그인을 한 경우 예외 발생
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.KAKAO) {
            logger.info("kakao user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else if (userEntity == null) {
            logger.info("creating kakao user $kakaoEmail")
            isNewUser = true
            socialUserService.createKakaoUser(kakaoProfile)
        } else {
            throw EmailAlreadyExistsException()
        }

        return SocialLoginResponse(
            accessToken = JwtUtil.generateAccessToken(kakaoEmail),
            refreshToken = JwtUtil.generateRefreshToken(kakaoEmail),
            isNewUser = isNewUser
        )
    }

    /**
     * 구글 프로필 정보를 받아온 후, 로그인 또는 회원가입 후 로그인 로직을 처리하는 함수
     */
    fun googleLogin(accessCode: String): SocialLoginResponse {
        val oAuthToken: GoogleOAuthToken = googleUtil.requestToken(accessCode)
        val googleProfile: GoogleProfile = googleUtil.requestProfile(oAuthToken)
        var isNewUser = false

        val googleEmail = googleProfile.email
        val userEntity = socialUserService.findUserByEmail(googleEmail)

        // 기존에 등록된 이메일이 있으면서 다른 서비스로 로그인을 한 경우 예외 발생
        val user: User = if (userEntity != null && userEntity.socialType == SocialType.GOOGLE) {
            logger.info("google user already exists: ${userEntity.id}, ${userEntity.email}")
            User.fromEntity(userEntity)
        } else if (userEntity == null) {
            logger.info("creating google user $googleEmail")
            isNewUser = true
            socialUserService.createGoogleUser(googleProfile)
        } else {
            throw EmailAlreadyExistsException()
        }

        return SocialLoginResponse(
            accessToken = JwtUtil.generateAccessToken(googleEmail),
            refreshToken = JwtUtil.generateRefreshToken(googleEmail),
            isNewUser = isNewUser
        )
    }
}
