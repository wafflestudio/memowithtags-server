package com.wafflestudio.toyproject.memoWithTags.tag.service

import com.wafflestudio.toyproject.memoWithTags.tag.TagNotFoundException
import com.wafflestudio.toyproject.memoWithTags.tag.WrongUserException
import com.wafflestudio.toyproject.memoWithTags.tag.controller.Tag
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagRepository
import com.wafflestudio.toyproject.memoWithTags.user.AuthenticationFailedException
import com.wafflestudio.toyproject.memoWithTags.user.contoller.User
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TagService(
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {
    fun getTags(user: User): List<Tag> {
        return tagRepository.findByUserId(user.id).map { Tag(it.id!!, it.name, it.color) }
    }

    fun createTag(name: String, color: String, user: User): Tag {
        val userEntity = userRepository.findByEmail(user.email) ?: throw AuthenticationFailedException()
        val tagEntity = TagEntity(name = name, color = color, user = userEntity)
        val savedTagEntity = tagRepository.save(tagEntity)
        return Tag(savedTagEntity.id!!, savedTagEntity.name, savedTagEntity.color)
    }

    fun updateTag(id: Long, name: String, color: String, user: User): Tag {
        val tagEntity = tagRepository.findById(id).orElseThrow { throw TagNotFoundException() }
        if (tagEntity.user.email != user.email) {
            throw WrongUserException()
        }
        tagEntity.name = name
        tagEntity.color = color
        tagEntity.updatedAt = Instant.now()
        val savedTagEntity = tagRepository.save(tagEntity)
        return Tag(savedTagEntity.id!!, savedTagEntity.name, savedTagEntity.color)
    }

    fun deleteTag(tagId: Long, user: User) {
        val tagEntity = tagRepository.findById(tagId).orElseThrow { throw TagNotFoundException() }
        if (tagEntity.user.email != user.email) {
            throw WrongUserException()
        }
        tagRepository.delete(tagEntity)
    }
}
