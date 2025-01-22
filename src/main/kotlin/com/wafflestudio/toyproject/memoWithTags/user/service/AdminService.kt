package com.wafflestudio.toyproject.memoWithTags.user.service

import com.wafflestudio.toyproject.memoWithTags.exception.UserNotAdminException
import com.wafflestudio.toyproject.memoWithTags.exception.UserNotFoundException
import com.wafflestudio.toyproject.memoWithTags.user.RoleType
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.user.dto.UserUpdateInfo
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AdminService(
    private val userRepository: UserRepository
) {
    /**
     * 관리자 여부를 리턴하는 함수
     */
    fun isAdmin(userId: UUID) {
        val userEntity = userRepository.findById(userId).orElseThrow { UserNotFoundException() }
        if (userEntity.role != RoleType.ROLE_ADMIN) {
            throw UserNotAdminException()
        }
    }

    /**
     * DB에 저장된 모든 User를 리턴하는 함수
     */
    fun getUsers(): List<User> {
        return userRepository.findAll().map { User.fromEntity(it) }
    }

    /**
     * 해당하는 Id의 User를 삭제하는 함수
     */
    fun deleteUser(userId: UUID) {
        val userEntity = userRepository.findById(userId).orElseThrow { UserNotFoundException() }
        userRepository.delete(userEntity)
    }

    /**
     * 해당하는 Id의 User를 userUpdateInfo에 따라 업데이트하는 함수
     */
    fun updateUser(userId: UUID, userUpdateInfo: UserUpdateInfo): User {
        val userEntity = userRepository.findById(userId).orElseThrow { UserNotFoundException() }
        userEntity.nickname = userUpdateInfo.nickname
        userEntity.hashedPassword = BCrypt.hashpw(userUpdateInfo.password, BCrypt.gensalt())
        userEntity.verified = userUpdateInfo.verified
        userEntity.role = userUpdateInfo.role
        userEntity.socialType = userUpdateInfo.socialType
        userRepository.save(userEntity)
        return User.fromEntity(userEntity)
    }
}
