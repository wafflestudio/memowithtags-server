package com.wafflestudio.toyproject.memoWithTags.user

import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: UserEntity) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String {
        return user.hashedPassword
    }

    override fun getUsername(): String {
        return user.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true // 계정 만료 여부 로직이 필요하면 수정
    }

    override fun isAccountNonLocked(): Boolean {
        return true // 계정 잠금 여부 로직이 필요하면 수정
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true // 자격 증명 만료 여부 로직이 필요하면 수정
    }

    override fun isEnabled(): Boolean {
        return true // 계정 활성화 여부 로직이 필요하면 수정
    }
}
