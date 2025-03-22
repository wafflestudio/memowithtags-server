package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object UpdatePasswordExceptionDocs {

    @field:ExplainError("유저가 검증되지 않았습니다.")
    val USER_AUTHENTICATION_FAILED = ErrorCode.USER_AUTHENTICATION_FAILED

    @field:ExplainError("유저를 찾을 수 없습니다.")
    val USER_NOT_FOUND = ErrorCode.USER_NOT_FOUND

    @field:ExplainError("비밀번호가 일치하지 않습니다.")
    val USER_UPDATE_PASSWORD_INVALID = ErrorCode.USER_UPDATE_PASSWORD_INVALID
}