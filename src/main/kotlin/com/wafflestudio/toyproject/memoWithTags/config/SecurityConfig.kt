package com.wafflestudio.toyproject.memoWithTags.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // CSRF 비활성화
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/**").permitAll() // 인증 없이 허용
                    .anyRequest().authenticated() // 나머지 요청은 인증 필요
            }
        return http.build()
    }
}
