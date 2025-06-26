package com.wafflestudio.toyproject.memoWithTags.memo.docs

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object RecommendMemoExceptionDocs {
    @field:ExplainError("유저 검증에 실패했을 때")
    val USER_AUTHENTICATION_FAILED = ErrorCode.USER_AUTHENTICATION_FAILED
}
