package com.wafflestudio.toyproject.memoWithTags.exception

open class CustomException(
    val errorCode: String,
    message: String
) : RuntimeException(message)
