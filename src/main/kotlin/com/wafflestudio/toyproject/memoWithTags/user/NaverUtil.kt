package com.wafflestudio.toyproject.memoWithTags.user

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.wafflestudio.toyproject.memoWithTags.exception.OAuthRequestException
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.NaverOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.NaverProfile
import com.wafflestudio.toyproject.memoWithTags.social_login.dto.NaverProfileResponse
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
import java.util.UUID

@Component
class NaverUtil(
    @Value("\${naver.auth.client-id}")
    private val naverClientId: String,
    @Value("\${naver.auth.client-secret}")
    private val naverClientSecret: String
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun requestToken(accessCode: String): NaverOAuthToken {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")

        val state = UUID.randomUUID().toString()
        val params: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", naverClientId)
            add("client_secret", naverClientSecret)
            add("code", accessCode)
            add("state", state)
        }

        val naverTokenRequest = HttpEntity(params, headers)
        logger.info("Token Request: $naverTokenRequest")

        val response: ResponseEntity<String> = restTemplate.exchange(
            "https://nid.naver.com/oauth2.0/token",
            HttpMethod.POST,
            naverTokenRequest,
            String::class.java
        )
        logger.info("Token Response: $response")

        val objectMapper = ObjectMapper().registerKotlinModule()

        return try {
            val oAuthToken = objectMapper.readValue(response.body, NaverOAuthToken::class.java)
            logger.info("oAuthToken: ${oAuthToken.access_token}")
            oAuthToken
        } catch (e: JsonProcessingException) {
            logger.info("Token processing error: ${e.message}")
            throw OAuthRequestException()
        }
    }

    fun requestProfile(oAuthToken: NaverOAuthToken): NaverProfile {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            add("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            add("Authorization", "Bearer ${oAuthToken.access_token}")
        }

        val naverProfileRequest: HttpEntity<MultiValueMap<String, String>> = HttpEntity(headers)
        logger.info("Profile Request: $naverProfileRequest")

        val response: ResponseEntity<String> = restTemplate.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.GET,
            naverProfileRequest,
            String::class.java
        )
        logger.info("Profile Response: $response")

        val objectMapper = ObjectMapper().registerKotlinModule()
        return try {
            val naverProfile = objectMapper.readValue(response.body, NaverProfileResponse::class.java)
            logger.info("naver email: $naverProfile")
            naverProfile.response
        } catch (e: JsonProcessingException) {
            logger.info("Profile processing error: ${e.message}")
            throw OAuthRequestException()
        }
    }
}
