package com.wafflestudio.toyproject.memoWithTags.user

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey

object JwtUtil {
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private const val ACCESS_TOKEN_EXPIRATION: Long = 1000 * 60 * 120 // 2시간
    private const val REFRESH_TOKEN_EXPIRATION: Long = 1000 * 60 * 60 * 24 * 14 // 14일

    // Access Token 생성
    fun generateAccessToken(userEmail: String): String {
        return Jwts.builder()
            .setSubject(userEmail)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .signWith(secretKey)
            .compact()
    }

    // Refresh Token 생성
    fun generateRefreshToken(userEmail: String): String {
        return Jwts.builder()
            .setSubject(userEmail)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .signWith(secretKey)
            .compact()
    }

    // Access Token 검증 및 userEmail
    fun validateAccessTokenGetUserId(accessToken: String): String? {
        return try {
            val claims =
                Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessToken)
                    .body
            if (claims.expiration.before(Date())) {
                null
            } else {
                claims.subject
            }
        } catch (e: Exception) {
            null
        }
    }
}

// catch (e: ExpiredJwtException) {
//    // 토큰 만료 처리
//    null
// } catch (e: MalformedJwtException) {
//    // 잘못된 토큰 처리
//    null
// } catch (e: Exception) {
//    // 기타 예외 처리
//    null
// } 예외 분리해서 이후 처리
