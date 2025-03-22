package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object VerifyEmailExceptionDocs {

    @field:ExplainError("이메일이 인증되지 않았습니다")
    val USER_MAIL_VERIFICATION_FAILED = ErrorCode.USER_MAIL_VERIFICATION_FAILED

}