package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

open class CustomException(
    val httpErrorCode: HttpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR, // 기본값 500
    val errorCode: Int,
    val msg: String,
    cause: Throwable? = null
) : RuntimeException(msg, cause) {
    override fun toString(): String {
        return "CustomException(message='$msg', errorCode=$errorCode, httpErrorCode=$httpErrorCode)"
    }
}
