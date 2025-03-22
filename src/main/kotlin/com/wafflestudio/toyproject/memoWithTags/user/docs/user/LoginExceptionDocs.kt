package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object LoginExceptionDocs {

    @field:ExplainError("이메일이 존재하지 않거나 잘못된 비밀번호입니다.")
    val USER_SIGN_IN_INVALID = ErrorCode.USER_SIGN_IN_INVALID

}