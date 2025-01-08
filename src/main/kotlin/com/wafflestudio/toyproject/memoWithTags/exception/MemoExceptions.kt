package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus

class AccessDeniedException : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.FORBIDDEN,
    msg = "해당 메모를 업데이트할 권한이 없습니다."
)

class MemoNotFoundException() : CustomException(
    errorCode = 0,
    httpErrorCode = HttpStatus.NOT_FOUND,
    msg = "존재하지 않는 메모입니다."
)
