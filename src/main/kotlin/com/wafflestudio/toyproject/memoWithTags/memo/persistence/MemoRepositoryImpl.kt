package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.wafflestudio.toyproject.memoWithTags.memo.controller.Memo
import com.wafflestudio.toyproject.memoWithTags.memo.dto.SearchResult
import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Subquery
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

/**/
@Repository
class MemoRepositoryImpl(
    private val em: EntityManager
) : MemoRepositoryCustom {

    override fun searchMemo(
        userId: UUID,
        content_text: String?,
        tags: List<Long>?,
        startDate: Instant?,
        endDate: Instant?,
        page: Int, // 몇 번째 페이지인지 (1부터 시작)
        pageSize: Int // 한 페이지에 몇 개를 가져올지
    ): SearchResult<Memo> {
        val cb: CriteriaBuilder = em.criteriaBuilder
        val query: CriteriaQuery<MemoEntity> = cb.createQuery(MemoEntity::class.java)
        val root = query.from(MemoEntity::class.java)

        // 조건들을 담을 리스트
        val predicates = mutableListOf<Predicate>()

        predicates.add(cb.equal(root.get<UserEntity>("user").get<UUID>("id"), userId)) // userId 조건

        // 1) 메모 내용 조건
        content_text?.let {
            // "content LIKE '%it%'" 조건
            predicates.add(cb.like(root.get("content"), "%$it%"))
        }

        // 2) 날짜 범위 조건 (createdAt 기준이라고 가정)
        startDate?.let {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), it))
        }
        endDate?.let {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), it))
        }

        // 3) 특정 태그 목록(tags)을 전부 포함하는 메모 검색
        if (!tags.isNullOrEmpty()) {
            // (A) 서브쿼리 작성
            val subquery: Subquery<Long> = query.subquery(Long::class.java)
            val subRoot = subquery.from(MemoEntity::class.java)

            // MemoEntity -> MemoTagEntity (1:N 관계)
            val subJoin = subRoot.join<MutableSet<MemoTagEntity>, MemoTagEntity>("memoTags")

            // MemoTagEntity -> TagEntity (N:1 관계)
            // MemoTagEntity 내부에 "tag" 필드가 있고, TagEntity의 PK가 Long id라고 가정
            val tagPath = subJoin.get<TagEntity>("tag")
            val tagIdPath = tagPath.get<Long>("id")

            subquery
                .select(subRoot.get<Long>("id"))
                .where(
                    cb.and(
                        // 메인 쿼리의 Memo와 동일한 id
                        cb.equal(subRoot.get<Long>("id"), root.get<Long>("id")),
                        // 이 메모가 갖고 있는 태그 ID가 검색 태그 목록 중 하나여야 함
                        tagIdPath.`in`(tags)
                    )
                )
                // 메모 하나당 여러 태그 -> group by id
                .groupBy(subRoot.get<Long>("id"))
                // having: countDistinct(tagId)가 "tags.size"와 같아야
                // -> 태그 목록을 전부 포함한다는 의미
                .having(
                    cb.equal(
                        cb.countDistinct(tagIdPath),
                        tags.size.toLong()
                    )
                )

            // 서브쿼리 결과(id 목록)에 메인 쿼리의 Memo id가 포함되어야 함
            predicates.add(
                cb.`in`(root.get<Long>("id")).value(subquery)
            )
        }

        // 4) WHERE 절 적용
        query.where(cb.and(*predicates.toTypedArray()))

        // 5) ORDER BY: createdAt DESC (최신 메모부터)
        query.orderBy(cb.desc(root.get<Instant>("createdAt")))

        // 6) 페이징(OFFSET, LIMIT) 설정
        val typedQuery = em.createQuery(query)
        // page=1일 때 firstResult=0, page=2일 때 firstResult=10, ...
        typedQuery.firstResult = (page - 1) * pageSize
        typedQuery.maxResults = pageSize

        // 7) 쿼리 실행 & 결과 리턴
        val results = typedQuery.resultList

        /**
         * 2) Count 쿼리 (전체 개수 구하기)
         *
         *  메인 쿼리와 동일한 조건을 사용하되,
         *  select 부분만 count(*) 또는 count(root)를 사용.
         */
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(MemoEntity::class.java)
        val countPredicates = mutableListOf<Predicate>()

        // 유저 조건
        countPredicates.add(cb.equal(countRoot.get<UserEntity>("user").get<UUID>("id"), userId))

        // 메모 내용 조건
        content_text?.let {
            countPredicates.add(cb.like(countRoot.get("content"), "%$it%"))
        }

        // 날짜 범위 조건
        startDate?.let {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("createdAt"), it))
        }
        endDate?.let {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("createdAt"), it))
        }

        // 태그 조건(전체 태그 포함) — “서브쿼리 + having countDistinct” 방식과 동일한 논리를 재활용
        // 다만, count 쿼리에서 똑같이 join/having을 해야 합니다.
        if (!tags.isNullOrEmpty()) {
            val subqueryForCount: Subquery<Long> = countQuery.subquery(Long::class.java)
            val subRootForCount = subqueryForCount.from(MemoEntity::class.java)

            val subJoinForCount = subRootForCount.join<MutableSet<MemoTagEntity>, MemoTagEntity>("memoTags")
            val tagPathForCount = subJoinForCount.get<TagEntity>("tag")
            val tagIdPathForCount = tagPathForCount.get<Long>("id")

            subqueryForCount
                .select(subRootForCount.get<Long>("id"))
                .where(
                    cb.and(
                        cb.equal(subRootForCount.get<Long>("id"), countRoot.get<Long>("id")),
                        tagIdPathForCount.`in`(tags)
                    )
                )
                .groupBy(subRootForCount.get<Long>("id"))
                .having(cb.equal(cb.countDistinct(tagIdPathForCount), tags.size.toLong()))

            countPredicates.add(cb.`in`(countRoot.get<Long>("id")).value(subqueryForCount))
        }

        // count 쿼리에 WHERE 절 적용
        countQuery.select(cb.count(countRoot))
        countQuery.where(cb.and(*countPredicates.toTypedArray()))

        // 실행해서 전체 개수 구함
        val totalResults = em.createQuery(countQuery).singleResult

        // 전체 페이지 수 = totalResults / pageSize (올림)
        val totalPages = if (totalResults == 0L) {
            1 // 검색 결과가 없으면 페이지는 1이라고 가정
        } else {
            ((totalResults - 1) / pageSize + 1).toInt()
        }

        return SearchResult<Memo>(
            page = page,
            totalPages = totalPages,
            totalResults = totalResults.toInt(),
            results = results.map { Memo.fromEntity(it) }
        )
    }
}
