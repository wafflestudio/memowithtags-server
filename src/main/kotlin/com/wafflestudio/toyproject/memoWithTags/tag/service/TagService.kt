package com.wafflestudio.toyproject.memoWithTags.tag.service

import com.wafflestudio.toyproject.memoWithTags.exception.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.exception.TagNotFoundException
import com.wafflestudio.toyproject.memoWithTags.exception.WrongUserException
import com.wafflestudio.toyproject.memoWithTags.tag.controller.Tag
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagRepository
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {
    fun getTags(user: User): List<Tag> {
        return tagRepository.findByUserId(user.id).map { Tag(it.id!!, it.name, it.colorHex) }
    }

    fun createTag(name: String, colorHex: String, user: User): Tag {
        val userEntity = userRepository.findByEmail(user.email) ?: throw AuthenticationFailedException()
        val tagEntity = TagEntity(name = name, colorHex = colorHex, user = userEntity)
        val savedTagEntity = tagRepository.save(tagEntity)
        return Tag(savedTagEntity.id!!, savedTagEntity.name, savedTagEntity.colorHex)
    }

    fun updateTag(id: Long, name: String, colorHex: String, user: User): Tag {
        val tagEntity = tagRepository.findById(id).orElseThrow { throw TagNotFoundException() }
        if (tagEntity.user.email != user.email) {
            throw WrongUserException()
        }
        tagEntity.name = name
        tagEntity.colorHex = colorHex
        tagEntity.updatedAt = Instant.now()
        val savedTagEntity = tagRepository.save(tagEntity)
        return Tag(savedTagEntity.id!!, savedTagEntity.name, savedTagEntity.colorHex)
    }

    fun deleteTag(tagId: Long, user: User) {
        val tagEntity = tagRepository.findById(tagId).orElseThrow { throw TagNotFoundException() }
        if (tagEntity.user.email != user.email) {
            throw WrongUserException()
        }
        tagRepository.delete(tagEntity)
    }
}
