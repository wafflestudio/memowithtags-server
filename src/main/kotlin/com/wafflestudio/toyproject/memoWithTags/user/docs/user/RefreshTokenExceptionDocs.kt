package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object RefreshTokenExceptionDocs {
    @field:ExplainError("유저가 검증되지 않았습니다.")
    val USER_AUTHENTICATION_FAILED = ErrorCode.USER_AUTHENTICATION_FAILED

    @field:ExplainError("토큰이 올바르지 않습니다")
    val TOKEN_INVALID = ErrorCode.TOKEN_INVALID

    @field:ExplainError("유저를 찾을 수 없습니다.")
    val USER_NOT_FOUND = ErrorCode.USER_NOT_FOUND
}
