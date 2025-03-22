package com.wafflestudio.toyproject.memoWithTags.user.docs.user

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExceptionDoc
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

@ExceptionDoc
object RegisterExceptionDocs {

    @field:ExplainError("이메일 형식이 올바르지 않은 경우")
    val USER_WRONG_EMAIL_FORMAT = ErrorCode.USER_WRONG_EMAIL_FORMAT

    @field:ExplainError("비밀번호 형식이 올바르지 않은 경우")
    val USER_WRONG_PASSWORD_FORMAT = ErrorCode.USER_WRONG_PASSWORD_FORMAT

    @field:ExplainError("닉네임 형식이 올바르지 않은 경우")
    val USER_WRONG_NICKNAME_FORMAT = ErrorCode.USER_WRONG_NICKNAME_FORMAT

    @field:ExplainError("이미 존재하는 이메일인 경우")
    val USER_EMAIL_ALREADY_EXISTS = ErrorCode.USER_EMAIL_ALREADY_EXISTS

    @field:ExplainError("이메일이 검증 되지 않은 경우")
    val USER_EMAIL_NOT_VERIFIED = ErrorCode.USER_EMAIL_NOT_VERIFIED
}