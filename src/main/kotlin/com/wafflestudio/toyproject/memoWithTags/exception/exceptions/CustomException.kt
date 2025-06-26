package com.wafflestudio.toyproject.memoWithTags.exception.exceptions

import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode

open class CustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message) {
    override fun toString(): String {
        return "CustomException(message='${errorCode.message}', errorCode=${errorCode.code}, httpErrorCode=${errorCode.status})"
    }
}
