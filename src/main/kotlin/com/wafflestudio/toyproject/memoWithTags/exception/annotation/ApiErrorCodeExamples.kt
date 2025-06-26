package com.wafflestudio.toyproject.memoWithTags.exception.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiErrorCodeExamples(val value: KClass<*>)
