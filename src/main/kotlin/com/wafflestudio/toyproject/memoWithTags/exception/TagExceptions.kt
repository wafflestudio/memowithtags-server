package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus

class TagNotFoundException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 만료되었습니다."
)
class WrongUserException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 만료되었습니다."
)
