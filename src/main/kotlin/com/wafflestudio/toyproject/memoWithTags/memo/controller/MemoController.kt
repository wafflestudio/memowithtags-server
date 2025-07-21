package com.wafflestudio.toyproject.memoWithTags.memo.controller

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ApiErrorCodeExamples
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.MemoNotFoundException
import com.wafflestudio.toyproject.memoWithTags.memo.docs.AddTagToMemoExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.memo.docs.CreateMemoExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.memo.docs.DeleteMemoExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.memo.docs.FetchPageFromMemoExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.memo.docs.RecommendMemoExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.memo.docs.SearchMemoExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.memo.docs.UpdateMemoExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.CreateMemoRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.MemoSearchRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.RecommendMemoRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.UpdateMemoRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoRequest.UpdateTagRequest
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoResponse.AddTagResponse
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoResponse.CreateMemoResponse
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoResponse.RecommendMemoResponse
import com.wafflestudio.toyproject.memoWithTags.memo.dto.MemoResponse.UpdateMemoResponse
import com.wafflestudio.toyproject.memoWithTags.memo.dto.SearchResult
import com.wafflestudio.toyproject.memoWithTags.memo.service.MemoService
import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@Tag(name = "memo api", description = "메모 관련 api")
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

    @Operation(summary = "메모 생성")
    @ApiErrorCodeExamples(CreateMemoExceptionDocs::class)
    @PostMapping("/api/v1/memo")
    fun createMemo(
        @RequestBody request: CreateMemoRequest,
        @AuthUser user: User
    ): CreateMemoResponse {
        val memo = memoService.createMemo(user, request.content, request.tagIds, locked = request.locked)
        return CreateMemoResponse(
            id = memo.id,
            content = memo.content,
            createdAt = memo.createdAt,
            updatedAt = memo.updatedAt,
            tagIds = memo.tagIds,
            locked = memo.locked
        )
    }

    @Operation(summary = "메모 업데이트")
    @ApiErrorCodeExamples(UpdateMemoExceptionDocs::class)
    @PutMapping("/api/v1/memo/{memoId}")
    fun updateMemo(
        @PathVariable memoId: Long,
        @RequestBody request: UpdateMemoRequest,
        @AuthUser user: User
    ): UpdateMemoResponse {
        val memo = memoService.updateMemo(userId = user.id, content = request.content, memoId = memoId, tagIds = request.tagIds, locked = request.locked)
        return UpdateMemoResponse(
            id = memo.id,
            content = memo.content,
            createdAt = memo.createdAt,
            updatedAt = memo.updatedAt,
            tagIds = memo.tagIds,
            locked = memo.locked
        )
    }

    @Operation(summary = "메모 삭제")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(DeleteMemoExceptionDocs::class)
    @DeleteMapping("/api/v1/memo/{memoId}")
    fun deleteMemo(
        @PathVariable memoId: Long,
        @AuthUser user: User
    ): ResponseEntity<Unit> {
        memoService.deleteMemo(memoId = memoId, userId = user.id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(summary = "메모 검색")
    @ApiErrorCodeExamples(SearchMemoExceptionDocs::class)
    @GetMapping("/api/v1/search-memo")
    fun searchMemo(
        @ModelAttribute request: MemoSearchRequest,
        @AuthUser user: User
    ): SearchResult<Memo> {
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

    @Operation(summary = "메모 페이지 조회")
    @ApiErrorCodeExamples(FetchPageFromMemoExceptionDocs::class)
    @GetMapping("/api/v1/memo/{memoId}")
    fun fetchPageFromMemo(@PathVariable memoId: Long, @AuthUser user: User): SearchResult<Memo> {
        return memoService.fetchPageFromMemo(userId = user.id, memoId = memoId, pageSize = 15)
    }

    @Operation(summary = "메모에 태그 추가")
    @ApiErrorCodeExamples(AddTagToMemoExceptionDocs::class)
    @PostMapping("/api/v1/memo/{memoId}/tag")
    fun addTagToMemo(
        @PathVariable memoId: Long,
        @AuthUser user: User,
        @RequestBody addTagRequest: UpdateTagRequest
    ): AddTagResponse {
        memoService.addTag(userId = user.id, memoId = memoId, tagId = addTagRequest.tagId)
        return AddTagResponse(
            tagId = addTagRequest.tagId
        )
    }

    @Operation(summary = "메모에 태그 삭제")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(DeleteMemoExceptionDocs::class)
    @DeleteMapping("/api/v1/memo/{memoId}/tag")
    fun deleteTagFromMemo(
        @PathVariable memoId: Long,
        @AuthUser user: User,
        @RequestBody deleteTagRequest: UpdateTagRequest
    ): ResponseEntity<Unit> {
        memoService.deleteTag(userId = user.id, memoId = memoId, tagId = deleteTagRequest.tagId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Operation(summary = "메모 추천")
    @ApiErrorCodeExamples(RecommendMemoExceptionDocs::class)
    @PostMapping("/api/v1/recommend-memo")
    fun recommendMemo(
        @AuthUser user: User,
        @RequestBody recommendMemoRequest: RecommendMemoRequest
    ): ResponseEntity<RecommendMemoResponse> {
        val memoIds = memoService.getMemoIdsByTagIds(userId = user.id, tagIds = recommendMemoRequest.tagIds)
        return ResponseEntity.ok(RecommendMemoResponse(memoIds))
    }

    @Operation(summary = "테스트")
    @GetMapping("/api/test")
    fun test() {
        println(Instant.now())
    }
}
