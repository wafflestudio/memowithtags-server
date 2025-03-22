package com.wafflestudio.toyproject.memoWithTags.social

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.OAuthRequestException
import com.wafflestudio.toyproject.memoWithTags.social.dto.KakaoOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social.dto.KakaoProfile
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class KakaoUtil(
    @Value("\${kakao.auth.client}")
    private val kakaoClient: String,
    @Value("\${kakao.auth.redirect}")
    private val kakaoRedirect: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun requestToken(accessCode: String): KakaoOAuthToken {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", kakaoClient)
            add("redirect_url", kakaoRedirect)
            add("code", accessCode)
        }

        val kakaoTokenRequest = HttpEntity(params, headers)
        logger.info("Token Request: $kakaoTokenRequest")

        val response: ResponseEntity<String> = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String::class.java
        )
        logger.info("Token Response: $response")

        val objectMapper = ObjectMapper().registerKotlinModule()

        return try {
            val oAuthToken = objectMapper.readValue(response.body, KakaoOAuthToken::class.java)
            logger.info("oAuthToken: ${oAuthToken.access_token}")
            oAuthToken
        } catch (e: JsonProcessingException) {
            logger.info("Token processing error: ${e.message}")
            throw OAuthRequestException()
        }
    }

    fun requestProfile(oAuthToken: KakaoOAuthToken): KakaoProfile {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            add("Authorization", "Bearer ${oAuthToken.access_token}")
        }

        val kakaoProfileRequest: HttpEntity<MultiValueMap<String, String>> = HttpEntity(headers)
        logger.info("Profile Request: $kakaoProfileRequest")

        val response: ResponseEntity<String> = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.GET,
            kakaoProfileRequest,
            String::class.java
        )
        logger.info("Profile Response: $response")

        val objectMapper = ObjectMapper().registerKotlinModule()
        return try {
            val kakaoProfile = objectMapper.readValue(response.body, KakaoProfile::class.java)
            logger.info("kakao email: ${kakaoProfile.kakao_account.email}")
            kakaoProfile
        } catch (e: JsonProcessingException) {
            logger.info("Profile processing error: ${e.message}")
            throw OAuthRequestException()
        }
    }
}
