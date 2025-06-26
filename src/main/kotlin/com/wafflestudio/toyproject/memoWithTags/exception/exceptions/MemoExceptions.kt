package com.wafflestudio.toyproject.memoWithTags.exception.exceptions

import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

class AccessDeniedException : CustomException(
    ErrorCode.MEMO_ACCESS_DENIED
)

class MemoNotFoundException() : CustomException(
    ErrorCode.MEMO_NOT_FOUND
)
