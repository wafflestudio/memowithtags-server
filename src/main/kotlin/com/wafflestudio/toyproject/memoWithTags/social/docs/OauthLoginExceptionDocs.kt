package com.wafflestudio.toyproject.memoWithTags.social.docs

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object OauthLoginExceptionDocs {

    @field:ExplainError("이메일이 이미 존재할 때")
    val EMAIL_ALREADY_EXISTS = ErrorCode.USER_EMAIL_ALREADY_EXISTS

}