package com.wafflestudio.toyproject.memoWithTags.memo.docs

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object FetchPageFromMemoExceptionDocs {
    @field:ExplainError("유저 검증에 실패했을 때")
    val USER_AUTHENTICATION_FAILED = ErrorCode.USER_AUTHENTICATION_FAILED

    @field:ExplainError("메모가 존재하지 않을 때")
    val MEMO_NOT_FOUND = ErrorCode.MEMO_NOT_FOUND

    @field:ExplainError("메모에 대한 접근 권한이 없을 때")
    val MEMO_ACCESS_DENIED = ErrorCode.MEMO_ACCESS_DENIED
}
