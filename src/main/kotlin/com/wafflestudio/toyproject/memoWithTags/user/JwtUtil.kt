package com.wafflestudio.toyproject.memoWithTags.user

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey

object JwtUtil {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor("your-fixed-secret-key-here-your-fixed-secret-key-here".toByteArray())
    private const val ACCESS_TOKEN_EXPIRATION: Long = 1000 * 60 * 120 // 2시간
    private const val REFRESH_TOKEN_EXPIRATION: Long = 1000 * 60 * 60 * 24 * 14 // 14일

    fun getAccessTokenExpiration(): Long { return ACCESS_TOKEN_EXPIRATION }

    // Access Token 생성
    fun generateAccessToken(userEmail: String): String {
        return Jwts.builder()
            .subject(userEmail)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .signWith(secretKey)
            .compact()
    }

    // Refresh Token 생성
    fun generateRefreshToken(userEmail: String): String {
        return Jwts.builder()
            .subject(userEmail)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .signWith(secretKey)
            .compact()
    }
    fun isValidToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun extractUserEmail(token: String): String? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
            claims.subject
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
