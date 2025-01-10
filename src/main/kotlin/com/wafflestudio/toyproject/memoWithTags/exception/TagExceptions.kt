package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus

class TagNotFoundException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.NOT_FOUND,
    msg = "존재하지 않는 태그입니다."
)
class WrongUserException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.FORBIDDEN,
    msg = "유저가 가지고 있는 태그가 아닙니다."
)
