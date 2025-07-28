package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import com.wafflestudio.toyproject.memoWithTags.memo.dto.SearchResult
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID
import kotlin.math.sqrt

/**/
@Repository
class MemoRepositoryImpl(
    private val em: EntityManager,
    private val objectMapper: ObjectMapper
) : MemoRepositoryCustom {

    private fun cosine(a: List<Float>, b: List<Float>): Double {
        var dot = 0.0
        var na = 0.0
        var nb = 0.0
        for (i in a.indices) {
            val x = a[i].toDouble()
            val y = b[i].toDouble()
            dot += x * y
            na += x * x
            nb += y * y
        }
        return dot / (sqrt(na) * sqrt(nb))
    }

    override fun searchMemo(
        userId: UUID,
        content: String?,
        contentEmbeddingVector: List<Float>?,
        tags: List<Long>?,
        startDate: Instant?,
        endDate: Instant?,
        page: Int, // 몇 번째 페이지인지 (1부터 시작)
        pageSize: Int, // 한 페이지에 몇 개를 가져올지
        simThreshold: Double?,
        extraTopK: Int
    ): SearchResult<Memo> {
        val cb: CriteriaBuilder = em.criteriaBuilder

        fun commonPred(root: Root<MemoEntity>, cb: CriteriaBuilder): MutableList<Predicate> {
            val p = mutableListOf<Predicate>()
            p += cb.equal(root.get<UserEntity>("user").get<UUID>("id"), userId)
            if (!tags.isNullOrEmpty()) {
                val sub = cb.createQuery(Long::class.java).subquery(Long::class.java)
                val sr = sub.from(MemoEntity::class.java)
                val join = sr.join<MutableSet<MemoTagEntity>, MemoTagEntity>("memoTags")
                val tagId = join.get<TagEntity>("tag").get<Long>("id")
                sub.select(sr.get<Long>("id"))
                    .where(
                        cb.and(
                            cb.equal(sr.get<Long>("id"), root.get<Long>("id")),
                            tagId.`in`(tags)
                        )
                    )
                    .groupBy(sr.get<Long>("id"))
                    .having(cb.equal(cb.countDistinct(tagId), tags!!.size.toLong()))
                p += cb.`in`(root.get<Long>("id")).value(sub)
            }
            return p
        }

        val q1 = cb.createQuery(MemoEntity::class.java)
        val r1 = q1.from(MemoEntity::class.java)
        val pred1 = commonPred(r1, cb)
        content?.let { pred1 += cb.like(r1.get("contentText"), "%$it%") }
        q1.select(r1).where(cb.and(*pred1.toTypedArray())).orderBy(cb.desc(r1.get<Instant>("createdAt")))
        val hitList = em.createQuery(q1)
            .setFirstResult((page - 1) * pageSize) // 페이징은 우선 문자열 결과에만 적용
            .setMaxResults(pageSize)
            .resultList

        // count of A (원하면)
        val totalHits = run {
            val cq = cb.createQuery(Long::class.java)
            val cr = cq.from(MemoEntity::class.java)
            val p = commonPred(cr, cb)
            content?.let { p += cb.like(cr.get("contentText"), "%$it%") }
            cq.select(cb.count(cr)).where(cb.and(*p.toTypedArray()))
            em.createQuery(cq).singleResult.toInt()
        }

        // ===== 2) tags-only 후보 B (content 제외) =====
        val extraList: List<MemoEntity> =
            if (contentEmbeddingVector == null) {
                emptyList()
            } else {
                val q2 = cb.createQuery(MemoEntity::class.java)
                val r2 = q2.from(MemoEntity::class.java)
                val pred2 = commonPred(r2, cb) // content 제외!
                q2.select(r2).where(cb.and(*pred2.toTypedArray()))
                    .orderBy(cb.desc(r2.get<Instant>("createdAt")))
                em.createQuery(q2)
                    .setMaxResults(extraTopK) // 상한
                    .resultList
                    .filterNot { e -> hitList.any { it.id == e.id } } // A에서 이미 나온 건 제외
            }

        // ===== 3) B에서 코사인 유사도 =====
        val extraScored: List<Pair<MemoEntity, Double>> =
            if (contentEmbeddingVector != null) {
                extraList.map { e ->
                    val vec: List<Float> = objectMapper.readValue(e.embeddingVector)
                    e to cosine(contentEmbeddingVector, vec)
                }
                    .filter { it.second >= (simThreshold ?: 0.0) }
                    .sortedByDescending { it.second }
            } else {
                emptyList<Pair<MemoEntity, Double>>()
            }

        // ===== 4) 결과 합치기 =====
        val merged = hitList.map { it to Double.NaN } + extraScored
        val totalResults = totalHits + extraScored.size // 페이지 기준 전체 개수
        val totalPages = if (totalResults == 0) 1 else ((totalResults - 1) / pageSize + 1)

        // hitList는 이미 page 처리. extra는 그 뒤로 붙임.
        val remainingSlots = pageSize - hitList.size
        val extraSlice = if (remainingSlots > 0) extraScored.take(remainingSlots) else emptyList()

        val finalPage = hitList.map { Memo.fromEntity(it) } +
            extraSlice.map { Memo.fromEntity(it.first) }

        return SearchResult(
            page = page,
            totalPages = totalPages,
            totalResults = totalResults,
            results = finalPage
        )
    }
}
