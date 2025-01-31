package com.wafflestudio.toyproject.memoWithTags.memo.service

import com.wafflestudio.toyproject.memoWithTags.exception.AccessDeniedException
import com.wafflestudio.toyproject.memoWithTags.exception.MemoNotFoundException
import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import com.wafflestudio.toyproject.memoWithTags.memo.dto.SearchResult
import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoEntity
import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoRepository
import com.wafflestudio.toyproject.memoWithTags.memo.persistence.MemoTagEntity
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagRepository
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import com.wafflestudio.toyproject.memoWithTags.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class MemoService(
    private val memoRepository: MemoRepository,
    private val tagRepository: TagRepository,
    private val userService: UserService
) {
    @Transactional
    fun createMemo(user: User, id: UUID, embeddingVector: List<Double>, content: String, tagIds: List<UUID>, locked: Boolean): Memo {
        val tags: List<TagEntity> = tagRepository.findAllById(tagIds)
        val userEntity = userService.getUserEntityByEmail(user.email)
        val memoEntity = MemoEntity(
            id = id,
            embeddingVector = embeddingVector,
            content = content,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            user = userEntity,
            locked = locked
        )

        val memoTags = tags.map { tag ->
            MemoTagEntity(memo = memoEntity, tag = tag)
        }

        memoEntity.memoTags.addAll(memoTags)
        return Memo.fromEntity(memoRepository.save(memoEntity))
    }

    @Transactional
    fun updateMemo(userId: UUID, content: String, memoId: UUID, tagIds: List<UUID>, locked: Boolean, embeddingVector: List<Double>): Memo {
        val memo = memoRepository.findById(memoId)
            .orElseThrow { MemoNotFoundException() }
        if (memo.user.id != userId) {
            throw AccessDeniedException()
        }
        val tags: List<TagEntity> = tagRepository.findAllById(tagIds)

        memo.memoTags.clear()

        val memoTags = tags.map { tag ->
            MemoTagEntity(memo = memo, tag = tag)
        }

        memo.memoTags.addAll(memoTags)
        memo.content = content
        memo.updatedAt = Instant.now()
        memo.locked = locked
        memo.embeddingVector = embeddingVector

        return Memo.fromEntity(memoRepository.save(memo))
    }

    @Transactional
    fun deleteMemo(userId: UUID, memoId: UUID) {
        val memo = memoRepository.findById(memoId).orElseThrow { MemoNotFoundException() }
        if (memo.user.id != userId) { throw AccessDeniedException() }
        memoRepository.delete(memo)
    }

    @Transactional
    fun searchMemo(userId: UUID, content: String?, tags: List<Long>?, startDate: Instant?, endDate: Instant?, page: Int, pageSize: Int): SearchResult<Memo> {
        return memoRepository.searchMemo(userId = userId, content = content, tags = tags, startDate = startDate, endDate = endDate, page = page, pageSize = pageSize)
    }
}
