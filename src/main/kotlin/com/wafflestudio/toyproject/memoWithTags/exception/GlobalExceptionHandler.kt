package com.wafflestudio.toyproject.memoWithTags.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handle(exception: CustomException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity
            .status(exception.httpErrorCode)
            .body(mapOf("error" to exception.msg, "errorCode" to exception.errorCode))
    }
}
