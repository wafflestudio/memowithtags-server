package com.wafflestudio.toyproject.memoWithTags.memo.controller

import com.wafflestudio.toyproject.memoWithTags.exception.MemoNotFoundException
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.CreateMemoRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.MemoSearchRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.UpdateMemoRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoResponse.CreateMemoResponse
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoResponse.UpdateMemoResponse
import com.wafflestudio.toyproject.memoWithTags.memo.dto.SearchResult
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
import java.util.UUID

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
        val memo = memoService.createMemo(user, request.id, request.embeddingVector, request.content, request.tagIds, locked = request.locked)
        return CreateMemoResponse(
            id = memo.id,
            embeddingVector = memo.embeddingVector,
            content = memo.content,
            createdAt = memo.createdAt,
            updatedAt = memo.updatedAt,
            tagIds = memo.tagIds,
            locked = memo.locked
        )
    }

    @PutMapping("/api/v1/memo/{memoId}")
    fun updateMemo(
        @PathVariable memoId: UUID,
        @RequestBody request: UpdateMemoRequest,
        @AuthUser user: User
    ): UpdateMemoResponse {
        val memo = memoService.updateMemo(userId = user.id, content = request.content, memoId = memoId, tagIds = request.tagIds, locked = request.locked, embeddingVector = request.embeddingVector)
        return UpdateMemoResponse(
            id = memo.id,
            content = memo.content,
            createdAt = memo.createdAt,
            updatedAt = memo.updatedAt,
            tagIds = memo.tagIds,
            locked = memo.locked,
            embeddingVector = memo.embeddingVector
        )
    }

    @DeleteMapping("/api/v1/memo/{memoId}")
    fun deleteMemo(@PathVariable memoId: UUID, @AuthUser user: User): ResponseEntity<Void> {
        memoService.deleteMemo(memoId = memoId, userId = user.id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/api/v1/search-memo")
    fun searchMemo(@ModelAttribute request: MemoSearchRequest, @AuthUser user: User): SearchResult<Memo> {
        return memoService.searchMemo(
            userId = user.id,
            content = request.content,
            tags = request.tagIds,
            startDate = request.startDate,
            endDate = request.endDate,
            page = request.page,
            pageSize = 15
        )
    }

    @GetMapping("/api/test")
    fun test() {
        println(Instant.now())
    }
}
