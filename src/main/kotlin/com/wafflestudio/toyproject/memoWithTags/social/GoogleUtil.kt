package com.wafflestudio.toyproject.memoWithTags.social

import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.OAuthRequestException
import com.wafflestudio.toyproject.memoWithTags.social.dto.GoogleOAuthToken
import com.wafflestudio.toyproject.memoWithTags.social.dto.GoogleProfile
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class GoogleUtil(
    @Value("\${google.auth.client-id}")
    private val googleClientId: String,
    @Value("\${google.auth.client-secret}")
    private val googleClientSecret: String,
    @Value("\${google.auth.redirect}")
    private val googleRedirectUri: String
) {
    private val logger = LoggerFactory.getLogger(GoogleUtil::class.java)

    fun requestToken(accessCode: String): GoogleOAuthToken {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val params = mapOf(
            "grant_type" to "authorization_code",
            "client_id" to googleClientId,
            "client_secret" to googleClientSecret,
            "redirect_uri" to googleRedirectUri,
            "code" to accessCode
        )

        val googleTokenRequest = HttpEntity(params, headers)
        logger.info("Token Request: $googleTokenRequest")

        val response = restTemplate.exchange(
            "https://oauth2.googleapis.com/token",
            HttpMethod.POST,
            googleTokenRequest,
            GoogleOAuthToken::class.java
        )
        logger.info("Token Response: $response")

        return try {
            val oAuthToken = response.body!!
            logger.info("oAuthToken: ${oAuthToken.access_token}")
            oAuthToken
        } catch (e: NullPointerException) {
            logger.info("Token processing error: ${e.message}")
            throw OAuthRequestException()
        }
    }

    fun requestProfile(oAuthToken: GoogleOAuthToken): GoogleProfile {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.setBearerAuth(oAuthToken.access_token)
        headers.contentType = MediaType.APPLICATION_JSON

        val kakaoProfileRequest: HttpEntity<MultiValueMap<String, String>> = HttpEntity(headers)
        logger.info("Profile Request: $kakaoProfileRequest")

        val response = restTemplate.exchange(
            "https://www.googleapis.com/userinfo/v2/me",
            HttpMethod.GET,
            kakaoProfileRequest,
            GoogleProfile::class.java
        )
        logger.info("Profile Response: $response")

        return try {
            val googleProfile = response.body!!
            logger.info("google email: ${googleProfile.email}")
            googleProfile
        } catch (e: NullPointerException) {
            logger.info("Profile processing error: ${e.message}")
            throw OAuthRequestException()
        }
    }
}
