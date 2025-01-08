package com.wafflestudio.toyproject.memoWithTags.memo.persistence

import com.wafflestudio.toyproject.memoWithTags.tag.persistence.TagEntity
import com.wafflestudio.toyproject.memoWithTags.user.persistence.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Subquery
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class MemoRepositoryImpl(
    private val em: EntityManager
) : MemoRepositoryCustom {

    override fun searchMemo(
        userId: UUID,
        content: String?,
        tags: List<Long>?,
        startDate: Instant?,
        endDate: Instant?,
        page: Int, // 몇 번째 페이지인지 (1부터 시작)
        pageSize: Int // 한 페이지에 몇 개를 가져올지
    ): List<MemoEntity> {
        val cb = em.criteriaBuilder
        val query = cb.createQuery(MemoEntity::class.java)
        val root = query.from(MemoEntity::class.java)

        // 조건들을 담을 리스트
        val predicates = mutableListOf<Predicate>()

        predicates.add(cb.equal(root.get<UserEntity>("user").get<UUID>("id"), userId))

        // 1) 메모 내용 조건
        content?.let {
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
        return typedQuery.resultList
    }
}
