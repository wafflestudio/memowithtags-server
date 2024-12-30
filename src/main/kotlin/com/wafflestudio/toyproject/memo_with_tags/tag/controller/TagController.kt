package com.wafflestudio.toyproject.memo_with_tags.tag.controller

@RestController
class TagController(
    private val tagService: TagService
) {
    @GetMapping("/api/v1/tag")
    fun getTags(): List<TagDto> {
    }

    @PostMapping("/api/v1/tag")
    fun createTag(@RequestBody request: CreateTagRequest): CreateTagResponse {
    }

    @PutMapping("/api/v1/tag/{tagId}")
    fun updateTag(@PathVariable id: Long, @RequestBody request: UpdateTagRequest): UpdateTagResponse {
    }

    @DeleteMapping("/api/v1/tag/{tagId}")
    fun deleteTag(@PathVariable id: Long) {
    }
}

data class TagDto(
    val id: Long,
    val name: String,
    val color: String,
)

data class CreateTagRequest(
    val name: String,
    val color: String
)

data class CreateTagResponse(
    val id: Long,
    val name: String,
    val color: String,
)

data class UpdateTagRequest(
    val name: String,
    val color: String
)

data class UpdateTagResponse(
    val id: Long,
    val name: String,
    val color: String,
)
