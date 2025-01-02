package com.wafflestudio.toyproject.memoWithTags.tag.controller

import com.wafflestudio.toyproject.memoWithTags.tag.service.TagService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(
    private val tagService: TagService
) {
    @GetMapping("/api/v1/tag")
    fun getTags(): List<TagDto> {
        return emptyList() // 빈 리스트 반환
    }

    @PostMapping("/api/v1/tag")
    fun createTag(@RequestBody request: CreateTagRequest): TagDto {
        return TagDto(0L, "", "") // 기본값으로 TagDto 반환
    }

    @PutMapping("/api/v1/tag/{tagId}")
    fun updateTag(@PathVariable id: Long, @RequestBody request: UpdateTagRequest): TagDto {
        return TagDto(0L, "", "") // 기본값으로 TagDto 반환
    }

    @DeleteMapping("/api/v1/tag/{tagId}")
    fun deleteTag(@PathVariable id: Long) {
        // 반환 타입이 Unit이므로 아무 작업 없이 비워 둬도 OK
    }
}

data class TagDto(
    val id: Long,
    val name: String,
    val color: String
)

data class CreateTagRequest(
    val name: String,
    val color: String
)

data class UpdateTagRequest(
    val name: String,
    val color: String
)
