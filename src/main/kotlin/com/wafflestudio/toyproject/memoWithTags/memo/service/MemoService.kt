package com.wafflestudio.toyproject.memoWithTags.memo.service

import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.AccessDeniedException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.MemoNotFoundException
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.TagNotFoundException
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
import org.jsoup.Jsoup

@Service
class MemoService(
    private val memoRepository: MemoRepository,
    private val tagRepository: TagRepository,
    private val userService: UserService
) {
    @Transactional
    fun createMemo(user: User, content: String, tagIds: List<Long>, locked: Boolean): Memo {
        val tags: List<TagEntity> = tagRepository.findAllById(tagIds)
        val userEntity = userService.getUserEntityByEmail(user.email)
        val doc = Jsoup.parse(content)
        val memoEntity = MemoEntity(
            contentHtml = content,
            contentText = doc.text(),
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
    fun updateMemo(userId: UUID, content: String, memoId: Long, tagIds: List<Long>, locked: Boolean): Memo {
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
        memo.contentHtml = content
        memo.contentText = Jsoup.parse(content).text()
        memo.updatedAt = Instant.now()
        memo.locked = locked

        return Memo.fromEntity(memoRepository.save(memo))
    }

    @Transactional
    fun deleteMemo(userId: UUID, memoId: Long) {
        val memo = memoRepository.findById(memoId).orElseThrow { MemoNotFoundException() }
        if (memo.user.id != userId) { throw AccessDeniedException() }
        memoRepository.delete(memo)
    }

    @Transactional
    fun addTag(userId: UUID, memoId: Long, tagId: Long) {
        val memo = memoRepository.findById(memoId).orElseThrow { MemoNotFoundException() }
        if (memo.user.id != userId) { throw AccessDeniedException() }
        val tag = tagRepository.findById(tagId).orElseThrow { TagNotFoundException() }
        val memoTag = MemoTagEntity(memo = memo, tag = tag)
        memo.memoTags.add(memoTag)
    }

    @Transactional
    fun deleteTag(userId: UUID, memoId: Long, tagId: Long) {
        val memo = memoRepository.findById(memoId).orElseThrow { MemoNotFoundException() }
        if (memo.user.id != userId) { throw AccessDeniedException() }
        val tag = tagRepository.findById(tagId).orElseThrow { TagNotFoundException() }
        val memoTag = memo.memoTags.find { it.tag.id == tagId } ?: return

        memo.memoTags.remove(memoTag) // orphanRemoval 때문에 여기까지 끝
    }

    @Transactional
    fun searchMemo(userId: UUID, content: String?, tags: List<Long>?, startDate: Instant?, endDate: Instant?, page: Int, pageSize: Int): SearchResult<Memo> {
        return memoRepository.searchMemo(userId = userId, content = content, tags = tags, startDate = startDate, endDate = endDate, page = page, pageSize = pageSize)
    }

    @Transactional
    fun getMemoIdsByTagIds(userId: UUID, tagIds: List<Long>): List<Long> {
        // 해당 유저의 태그를 모두 가져옴
        val tags: List<TagEntity> = tagRepository.findAllById(tagIds).filter {
            it.user.id == userId
        }

        // 각 TagEntity의 memoTags 컬렉션에서 memo의 id를 추출 후 모두 flat하게 합침
        return tags.flatMap { tag ->
            tag.memoTags.sortedByDescending { memoTag ->
                memoTag.memo.createdAt
            }.mapNotNull { memoTag ->
                memoTag.memo.id
            }
        }
    }

    @Transactional
    fun fetchPageFromMemo(userId: UUID, memoId: Long, pageSize: Int): SearchResult<Memo> {
        val memo = memoRepository.findById(memoId).orElseThrow { MemoNotFoundException() }
        if (memo.user.id != userId) { throw AccessDeniedException() }
        val totalResults = memoRepository.searchMemo(userId = userId, content = null, tags = null, startDate = null, endDate = null, page = 1, pageSize = pageSize).totalResults
        val page = totalResults / pageSize + 1

        var results: SearchResult<Memo>? = null

        for (i in 1..page) {
            results = memoRepository.searchMemo(userId = userId, content = null, tags = null, startDate = null, endDate = null, page = i, pageSize = pageSize)
            if (results.results.any { it.id == memoId }) {
                break
            }
        }
        return results ?: throw MemoNotFoundException()
    }
}
