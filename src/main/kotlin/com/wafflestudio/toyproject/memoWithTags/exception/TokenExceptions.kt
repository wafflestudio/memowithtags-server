package com.wafflestudio.toyproject.memoWithTags.exception
import org.springframework.http.HttpStatus

class InValidTokenException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 유효하지 않습니다."
)
