package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus

class TokenExpiredException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 만료되었습니다."
)

class InvalidTokenSignatureException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰의 서명이 유효하지 않습니다."
)

class InValidTokenException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 유효하지 않습니다."
)
