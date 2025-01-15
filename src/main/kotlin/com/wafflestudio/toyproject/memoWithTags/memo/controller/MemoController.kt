package com.wafflestudio.toyproject.memoWithTags.memo.controller

import com.wafflestudio.toyproject.memoWithTags.exception.MemoNotFoundException
import com.wafflestudio.toyproject.memoWithTags.memo.service.MemoService
import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class MemoController(
    private val memoService: MemoService
) {
    @ExceptionHandler(AccessDeniedException::class)
    fun handleUnauthorizedException(e: AccessDeniedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
    }

    @ExceptionHandler(MemoNotFoundException::class)
    fun handleNotFoundException(e: MemoNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }

    @PostMapping("/api/v1/memo")
    fun createMemo(
        @RequestBody request: CreateMemoRequest,

        @AuthUser user: User
    ): CreateMemoResponse {
        val memo = memoService.createMemo(user, request.content, request.tagIds)
        return CreateMemoResponse(
            id = memo.id,
            content = memo.content,
            createdAt = memo.createdAt,
            updatedAt = memo.updatedAt,
            tagIds = memo.tagIds
        )
    }

    @PutMapping("/api/v1/memo/{memoId}")
    fun updateMemo(
        @PathVariable memoId: Long,
        @RequestBody request: UpdateMemoRequest,
        @AuthUser user: User
    ): UpdateMemoResponse {
        val memo = memoService.updateMemo(userId = user.id, content = request.content, memoId = memoId)
        return UpdateMemoResponse(
            id = memo.id,
            content = memo.content,
            createdAt = memo.createdAt,
            updatedAt = memo.updatedAt,
            tagIds = memo.tagIds
        )
    }

    @DeleteMapping("/api/v1/memo/{memoId}")
    fun deleteMemo(@PathVariable memoId: Long, @AuthUser user: User): ResponseEntity<Void> {
        memoService.deleteMemo(memoId = memoId, userId = user.id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/api/v1/search-memo")
    fun searchMemo(@ModelAttribute request: MemoSearchRequest, @AuthUser user: User): PagedResponse<Memo> {
        val results = memoService.searchMemo(
            userId = user.id,
            content = request.content,
            tags = request.tagIds,
            startDate = request.startDate,
            endDate = request.endDate,
            page = request.page,
            pageSize = 10
        )
        return PagedResponse(request.page, results)
    }

    @PostMapping("/api/v1/memo/{memoId}/tag")
    fun addTagToMemo(@PathVariable memoId: Long, @AuthUser user: User, @RequestBody addTagRequest: UpdateTagRequest): AddTagResponse {
        memoService.addTag(userId = user.id, memoId = memoId, tagId = addTagRequest.tagId)
        return AddTagResponse(
            tagId = addTagRequest.tagId
        )
    }

    @DeleteMapping("/api/v1/memo/{memoId}/tag")
    fun deleteTagFromMemo(@PathVariable memoId: Long, @AuthUser user: User, @RequestBody deleteTagRequest: UpdateTagRequest): ResponseEntity<Void> {
        memoService.deleteTag(userId = user.id, memoId = memoId, tagId = deleteTagRequest.tagId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/api/test")
    fun test() {
        println(Instant.now())
    }
}

data class CreateMemoRequest(
    val content: String,
    val tagIds: List<Long>
)

data class UpdateTagRequest(
    val tagId: Long
)

data class AddTagResponse(
    val tagId: Long
)

data class CreateMemoResponse(
    val id: Long,
    val content: String,
    val tagIds: List<Long>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class UpdateMemoRequest(
    val content: String
)

data class UpdateMemoResponse(
    val id: Long,
    val content: String,
    val tagIds: List<Long>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class MemoSearchRequest(
    val content: String? = null, // 검색할 텍스트 (optional)
    val tagIds: List<Long>? = null, // 태그 ID 배열 (optional, 콤마로 구분된 문자열 처리)
    val startDate: Instant? = null, // 검색 시작 날짜 (optional, ISO 8601 형식)
    val endDate: Instant? = null, // 검색 종료 날짜 (optional, ISO 8601 형식)
    val page: Int // 페이지 번호 (required)
)

data class PagedResponse<T>(
    val page: Int, // 현재 페이지 번호
    val results: List<T> // 결과 리스트 (제네릭으로 처리)
)
