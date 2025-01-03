import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date
import javax.crypto.SecretKey

object UserAccessTokenUtil {
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private const val ACCESS_TOKEN_EXPIRATION: Long = 1000 * 60 * 120 // 2시간
    private const val REFRESH_TOKEN_EXPIRATION: Long = 1000 * 60 * 60 * 24 * 14 // 14일

    // Access Token 생성
    fun generateAccessToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .signWith(secretKey)
            .compact()
    }

    // Refresh Token 생성
    fun generateRefreshToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .signWith(secretKey)
            .compact()
    }

    // Access Token 검증
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 사용자 Id
    fun extractUserId(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}
