package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus

class TagNotFoundException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.NOT_FOUND,
    msg = "존재하지 않는 태그입니다."
)
class WrongUserException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 만료되었습니다."
)
