package com.wafflestudio.toyproject.memoWithTags.exception.annotation

import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorCodeExamples(val value: KClass<*>)
