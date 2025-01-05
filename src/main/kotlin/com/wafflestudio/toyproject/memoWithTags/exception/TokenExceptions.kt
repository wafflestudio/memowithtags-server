package com.wafflestudio.toyproject.memoWithTags.exception

class TokenExpiredException : CustomException(
    "TOKEN_EXPIRED",
    "토큰이 만료되었습니다."
)

class InvalidTokenSignatureException : CustomException(
    "INVALID_TOKEN_SIGNATURE",
    "토큰의 서명이 유효하지 않습니다."
)

class InValidTokenException : CustomException(
    "INVALID_TOKEN",
    "토큰이 유효하지 않습니다."
)
