package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object ResetPasswordExceptionDocs {

    @field:ExplainError("이메일이 검증 되지 않은 경우")
    val USER_EMAIL_NOT_VERIFIED = ErrorCode.USER_EMAIL_NOT_VERIFIED

    @field:ExplainError("유저가 존재하지 않는 경우")
    val USER_NOT_FOUND = ErrorCode.USER_NOT_FOUND
}