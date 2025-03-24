package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object SendEmailExceptionDocs {

    @field:ExplainError("이메일 전송에 실패했습니다.")
    val USER_UNABLE_TO_SEND_EMAIL = ErrorCode.USER_UNABLE_TO_SEND_EMAIL
}
