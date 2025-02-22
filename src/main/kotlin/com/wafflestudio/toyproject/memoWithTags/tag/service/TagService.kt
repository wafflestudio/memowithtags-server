package com.wafflestudio.toyproject.memoWithTags.tag.service

import com.wafflestudio.toyproject.memoWithTags.exception.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.exception.TagNotFoundException
import com.wafflestudio.toyproject.memoWithTags.exception.TagNotOwnedByUserException
import com.wafflestudio.toyproject.memoWithTags.tag.controller.Tag
import com.wafflestudio.toyproject.memoWithTags.tag.dto.TagRequest.CreateTagRequest
import com.wafflestudio.toyproject.memoWithTags.tag.dto.TagRequest.UpdateTagRequest
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagRepository
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import java.time.Instant
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {
    fun getTags(user: User): List<Tag> {
        return tagRepository.findByUserId(user.id).map { Tag(it.id!!, it.name, it.colorHex, it.createdAt, it.updatedAt) }
    }

    fun createTag(request: CreateTagRequest, user: User): Tag {
        val userEntity = userRepository.findByEmail(user.email) ?: throw AuthenticationFailedException()
        val tagEntity = TagEntity(
            name = request.name,
            colorHex = request.colorHex,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            user = userEntity
        )
        val savedTagEntity = tagRepository.save(tagEntity)
        return Tag(
            savedTagEntity.id!!,
            savedTagEntity.name,
            savedTagEntity.colorHex,
            savedTagEntity.createdAt,
            savedTagEntity.updatedAt
        )
    }

    fun updateTag(tagId: Long, request: UpdateTagRequest, user: User): Tag {
        val tagEntity = tagRepository.findById(tagId).orElseThrow { throw TagNotFoundException() }
        if (tagEntity.user.email != user.email) {
            throw TagNotOwnedByUserException()
        }
        tagEntity.name = request.name
        tagEntity.colorHex = request.colorHex
        tagEntity.updatedAt = Instant.now()
        val savedTagEntity = tagRepository.save(tagEntity)
        return Tag(
            savedTagEntity.id!!,
            savedTagEntity.name,
            savedTagEntity.colorHex,
            savedTagEntity.createdAt,
            savedTagEntity.updatedAt
        )
    }

    fun deleteTag(tagId: Long, user: User) {
        val tagEntity = tagRepository.findById(tagId).orElseThrow { throw TagNotFoundException() }
        if (tagEntity.user.email != user.email) {
            throw TagNotOwnedByUserException()
        }
        tagRepository.delete(tagEntity)
    }
}
