package com.wafflestudio.toyproject.memoWithTags.exception.handler

import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.CustomException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.errorCode)
        return ResponseEntity.status(ex.errorCode.status).body(errorResponse)
    }
}
