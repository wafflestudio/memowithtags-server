package com.wafflestudio.toyproject.memo_with_tags.memo.controller

@RestController
class MemoController(
    private val memoService: MemoService
) {
    @PostMapping("/api/v1/memo")
    fun createMemo(@RequestBody request: CreateMemoRequest): CreateMemoResponse {
    }

    @PutMapping("/api/v1/memo/{memoId}")
    fun updateMemo(@PathVariable memoId: Long, @RequestBody request: UpdateMemoRequest): UpdateMemoResponse {
    }

    @DeleteMapping("/api/v1/memo/{memoId}")
    fun deleteMemo(@PathVariable memoId: Long) {
    }
}

data class CreateMemoRequest(
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

data class UpdateMemoRequest(
    val content: String,
    val tags: List<Long>
)

data class UpdateMemoResponse(
    val id: Long,
    val content: String,
    val tags: List<Long>,
    val createdAt: Instant,
    val updatedAt: Instant
)