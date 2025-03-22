package com.wafflestudio.toyproject.memoWithTags.exception.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ExplainError(val value: String)
