package com.wafflestudio.toyproject.memoWithTags.config

import com.wafflestudio.toyproject.memoWithTags.user.JwtUtil
import com.wafflestudio.toyproject.memoWithTags.user.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userService: UserService
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // 헤더에서 Authorization 추출
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        // 토큰 추출
        val jwtToken = authHeader.substring(7) // "Bearer " 이후의 부분

        try {
            // 토큰에서 사용자 이메일 추출
            val userEmail = JwtUtil.extractUserEmail(jwtToken)
            if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
                // 사용자 정보 로드 및 인증 객체 생성
                val userDetails = userService.loadUserByUsername(userEmail)
                if (JwtUtil.isValidToken(jwtToken)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        } catch (e: Exception) {
            logger.error("JWT Authentication failed: ${e.message}")
        }

        filterChain.doFilter(request, response)
    }
}