package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.dto.AdminRequest.CreateUserRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.AdminRequest.UpdateUserRequest
import com.wafflestudio.toyproject.memoWithTags.user.service.AdminService
import com.wafflestudio.toyproject.memoWithTags.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Tag(name = "admin api", description = "관리자 관련 api")
@RestController
@RequestMapping("/api/v1")
class AdminController(
    private val adminService: AdminService,
    private val userService: UserService
) {
    @Operation(summary = "유저 목록 가져오기")
    @GetMapping("/admin/user")
    fun getUsers(@AuthUser user: User): List<User> {
        adminService.assertAdminRole(user.id)
        return adminService.getUsers()
    }

    @Operation(summary = "유저 계정 생성")
    @PostMapping("/admin/user")
    fun createUser(@AuthUser user: User, @RequestBody request: CreateUserRequest): User {
        adminService.assertAdminRole(user.id)
        return userService.register(request.email, request.password, request.nickname)
    }

    @Operation(summary = "유저 계정 삭제하기")
    @DeleteMapping("/admin/user/{userId}")
    fun deleteUser(@AuthUser user: User, @PathVariable userId: UUID): ResponseEntity<Unit> {
        adminService.assertAdminRole(user.id)
        adminService.deleteUser(userId)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "유저 계정 업데이트")
    @PutMapping("/admin/user")
    fun getUsers(@AuthUser user: User, @RequestBody request: UpdateUserRequest): User {
        adminService.assertAdminRole(user.id)
        return adminService.updateUser(request.id, request.userUpdateInfo)
    }
}
