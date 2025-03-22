package com.wafflestudio.toyproject.memoWithTags.tag.docs

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object DeleteTagExceptionDocs {

        @field:ExplainError("유저 검증에 실패했을 때")
        val USER_AUTHENTICATION_FAILED = ErrorCode.USER_AUTHENTICATION_FAILED

        @field:ExplainError("태그가 존재하지 않을 때")
        val TAG_NOT_FOUND = ErrorCode.TAG_NOT_FOUND

        @field:ExplainError("태그가 유저에 의해 소유되지 않았을 때")
        val TAG_NOT_OWNED_BY_USER = ErrorCode.TAG_NOT_OWNED_BY_USER
}