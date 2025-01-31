package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.exception.UserNotFoundException
import com.wafflestudio.toyproject.memoWithTags.user.CustomUserDetails
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    /**
     * SecurityConfig에서 쓰이는 쿼리 함수
     */
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        return CustomUserDetails(user)
    }
}
