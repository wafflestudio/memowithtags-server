package com.wafflestudio.toyproject.memoWithTags.tag.controller
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ApiErrorCodeExamples
import com.wafflestudio.toyproject.memoWithTags.tag.docs.CreateTagExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.tag.docs.DeleteTagExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.tag.docs.GetTagsExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.tag.docs.UpdateTagExceptionDocs
import com.wafflestudio.toyproject.memoWithTags.tag.dto.TagRequest.CreateTagRequest
import com.wafflestudio.toyproject.memoWithTags.tag.dto.TagRequest.UpdateTagRequest
import com.wafflestudio.toyproject.memoWithTags.tag.service.TagService
import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@io.swagger.v3.oas.annotations.tags.Tag(name = "tag api", description = "태그 관련 api")
@RestController
class TagController(
    private val tagService: TagService
) {
    @Operation(summary = "태그 목록 조회")
    @ApiErrorCodeExamples(GetTagsExceptionDocs::class)
    @GetMapping("/api/v1/tag")
    fun getTags(@AuthUser user: User): List<Tag> {
        return tagService.getTags(user)
    }

    @Operation(summary = "태그 생성")
    @ApiErrorCodeExamples(CreateTagExceptionDocs::class)
    @PostMapping("/api/v1/tag")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTag(@RequestBody request: CreateTagRequest, @AuthUser user: User): Tag {
        val tag = tagService.createTag(request, user)
        return tag
    }

    @Operation(summary = "태그 수정")
    @ApiErrorCodeExamples(UpdateTagExceptionDocs::class)
    @PutMapping("/api/v1/tag/{tagId}")
    fun updateTag(@PathVariable tagId: Long, @RequestBody request: UpdateTagRequest, @AuthUser user: User): Tag {
        return tagService.updateTag(tagId, request, user)
    }

    @Operation(summary = "태그 삭제")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiErrorCodeExamples(DeleteTagExceptionDocs::class)
    @DeleteMapping("/api/v1/tag/{tagId}")
    fun deleteTag(@PathVariable tagId: Long, @AuthUser user: User): ResponseEntity<Unit> {
        tagService.deleteTag(tagId, user)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}
