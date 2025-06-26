package com.wafflestudio.toyproject.memoWithTags.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // CSRF 비활성화
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/**").permitAll() // 인증 없이 허용
                    .anyRequest().authenticated() // 나머지 요청은 인증 필요
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 X
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java) // JWT 필터 추가
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
