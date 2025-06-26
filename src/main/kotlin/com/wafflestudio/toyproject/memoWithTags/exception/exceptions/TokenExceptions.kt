package com.wafflestudio.toyproject.memoWithTags.exception.exceptions
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

class TokenExpiredException : CustomException(
    ErrorCode.TOKEN_EXPIRED
)

class InvalidTokenSignatureException : CustomException(
    ErrorCode.TOKEN_INVALID_SIGNATURE
)

class InValidTokenException : CustomException(
    ErrorCode.TOKEN_INVALID
)
