package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object WithdrawalExceptionDocs {
    @field:ExplainError("유저가 검증되지 않았습니다.")
    val USER_AUTHENTICATION_FAILED = ErrorCode.USER_AUTHENTICATION_FAILED

    @field:ExplainError("이메일이 일치하지 않습니다.")
    val USER_EMAIL_NOT_MATCHED = ErrorCode.USER_EMAIL_NOT_MATCHED
}
