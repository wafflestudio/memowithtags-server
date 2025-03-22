package com.wafflestudio.toyproject.memoWithTags.user.docs.admin

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object DeleteUserExceptionDocs {
    @field:ExplainError("유저 검증에 실패했을 때")
    val USER_AUTHENTICATION_FAILED = ErrorCode.USER_AUTHENTICATION_FAILED

    @field:ExplainError("admin이 아닌 경우")
    val USER_NOT_ADMIN = ErrorCode.USER_NOT_ADMIN

    @field:ExplainError("존재하지 않는 유저일 때")
    val USER_NOT_FOUND = ErrorCode.USER_NOT_FOUND
}