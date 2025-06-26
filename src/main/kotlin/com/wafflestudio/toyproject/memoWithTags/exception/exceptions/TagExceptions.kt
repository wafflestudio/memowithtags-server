package com.wafflestudio.toyproject.memoWithTags.exception.exceptions

import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

class TagNotFoundException : CustomException(
    ErrorCode.TAG_NOT_FOUND
)

class TagNotOwnedByUserException : CustomException(
    ErrorCode.TAG_NOT_OWNED_BY_USER
)
