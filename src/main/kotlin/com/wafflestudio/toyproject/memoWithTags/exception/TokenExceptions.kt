package com.wafflestudio.toyproject.memoWithTags.exception
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

sealed class TokenExceptions(
    httpErrorCode: HttpStatusCode,
    errorCode: Int,
    msg: String
) : CustomException(httpErrorCode, errorCode, msg)

class TokenExpiredException : TokenExceptions(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 만료되었습니다."
)

class InvalidTokenSignatureException : TokenExceptions(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰의 서명이 유효하지 않습니다."
)

class InValidTokenException : TokenExceptions(
    errorCode = 0,
    httpErrorCode = HttpStatus.UNAUTHORIZED,
    msg = "토큰이 유효하지 않습니다."
)
