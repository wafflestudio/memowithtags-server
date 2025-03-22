package com.wafflestudio.toyproject.memoWithTags.exception.handler

import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String
) {
    constructor(errorCode: ErrorCode) : this(
        status = errorCode.status,
        code = errorCode.code,
        message = errorCode.message
    )
}
