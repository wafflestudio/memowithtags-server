package com.wafflestudio.toyproject.memoWithTags.user

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Parameter(
    name = "Authorization",
    description = "JWT access token",
    required = true,
    `in` = ParameterIn.HEADER,
    schema = Schema(type = "string")
)
annotation class AuthUser
