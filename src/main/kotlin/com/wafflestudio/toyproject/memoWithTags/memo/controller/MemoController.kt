package com.wafflestudio.toyproject.memoWithTags.memo.controller

import com.wafflestudio.toyproject.memoWithTags.memo.service.MemoService
import org.springframework.web.bind.annotation.RestController

@RestController
class MemoController(
    private val memoService: MemoService
) {
    @PostMapping("/api/v1/memo")
    fun createMemo(@RequestBody request: CreateMemoRequest): MemoDto {
    }

    @PutMapping("/api/v1/memo/{memoId}")
    fun updateMemo(@PathVariable memoId: Long, @RequestBody request: UpdateMemoRequest): MemoDto {
    }

    @DeleteMapping("/api/v1/memo/{memoId}")
    fun deleteMemo(@PathVariable memoId: Long) {
    }
}

data class MemoDto(
    val content: String,
    val tags: List<Long>
)

data class CreateMemoResponse(
    val id: Long,
    val content: String,
    val tags: List<Long>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class UpdateMemoResponse(
    val id: Long,
    val content: String,
    val tags: List<Long>,
    val createdAt: Instant,
    val updatedAt: Instant
)