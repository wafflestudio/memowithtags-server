package com.wafflestudio.toyproject.memoWithTags.user.controller

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ApiErrorCodeExamples
import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.docs.admin.CreateUserExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.admin.DeleteUserExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.admin.GetUsersExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.docs.admin.UpdateUserExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.user.dto.AdminRequest.CreateUserRequest
import com.wafflestudio.toyproject.memoWithTags.user.dto.AdminRequest.UpdateUserRequest
import com.wafflestudio.toyproject.memoWithTags.user.service.AdminService
import com.wafflestudio.toyproject.memoWithTags.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*

@Tag(name = "admin api", description = "관리자 관련 api")
@RestController
@RequestMapping("/api/v1")
class AdminController(
    private val adminService: AdminService,
    private val userService: UserService
) {
    @ApiErrorCodeExamples(GetUsersExceptionDocs::class)
    @Operation(summary = "유저 목록 가져오기")
    @GetMapping("/admin/user")
    fun getUsers(@AuthUser user: User): List<User> {
        adminService.assertAdminRole(user.id)
        return adminService.getUsers()
    }

    @ApiErrorCodeExamples(CreateUserExceptionDocs::class)
    @Operation(summary = "유저 계정 생성")
    @PostMapping("/admin/user")
    fun createUser(@AuthUser user: User, @RequestBody request: CreateUserRequest): User {
        adminService.assertAdminRole(user.id)
        return userService.register(request.email, request.password, request.nickname)
    }

    @ApiErrorCodeExamples(DeleteUserExceptionDocs::class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "유저 계정 삭제하기")
    @DeleteMapping("/admin/user/{userId}")
    fun deleteUser(@AuthUser user: User, @PathVariable userId: UUID): ResponseEntity<Unit> {
        adminService.assertAdminRole(user.id)
        adminService.deleteUser(userId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @ApiErrorCodeExamples(UpdateUserExceptionDocs::class)
    @Operation(summary = "유저 계정 업데이트")
    @PutMapping("/admin/user")
    fun updateUser(@AuthUser user: User, @RequestBody request: UpdateUserRequest): User {
        adminService.assertAdminRole(user.id)
        return adminService.updateUser(request.id, request.userUpdateInfo)
    }
}
