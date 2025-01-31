package com.wafflestudio.toyproject.memoWithTags.tag.controller
import com.wafflestudio.toyproject.memoWithTags.tag.dto.TagRequest.CreateTagRequest
import com.wafflestudio.toyproject.memoWithTags.tag.dto.TagRequest.UpdateTagRequest
import com.wafflestudio.toyproject.memoWithTags.tag.service.TagService
import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import java.util.UUID
import org.springframework.http.ResponseEntity
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
    fun getTags(@AuthUser user: User): List<Tag> {
        return tagService.getTags(user)
    }

    @PostMapping("/api/v1/tag")
    fun createTag(@RequestBody request: CreateTagRequest, @AuthUser user: User): Tag {
        val tag = tagService.createTag(request, user)
        return tag
    }

    @PutMapping("/api/v1/tag/{tagId}")
    fun updateTag(@PathVariable tagId: UUID, @RequestBody request: UpdateTagRequest, @AuthUser user: User): Tag {
        return tagService.updateTag(request, user)
    }

    @DeleteMapping("/api/v1/tag/{tagId}")
    fun deleteTag(@PathVariable tagId: UUID, @AuthUser user: User): ResponseEntity<Unit> {
        tagService.deleteTag(tagId, user)
        return ResponseEntity.noContent().build()
    }
}
