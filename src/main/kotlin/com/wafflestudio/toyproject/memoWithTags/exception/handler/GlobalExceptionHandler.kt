package com.wafflestudio.toyproject.memoWithTags.exception.handler

import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode
import com.wafflestudio.toyproject.memoWithTags.exception.exceptions.CustomException
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(ex: CustomException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(ex.errorCode)
        return ResponseEntity.status(ex.errorCode.status).body(errorResponse)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleValidationException(e: HttpMessageNotReadableException?): ResponseEntity<ErrorResponse> {
        val error = ErrorCode.SERVER_HTTP_MESSAGE_NOT_READABLE
        val errorResponse = ErrorResponse(error)
        return ResponseEntity.status(error.status).body(errorResponse)
    }
}
