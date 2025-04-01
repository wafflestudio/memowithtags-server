package com.wafflestudio.toyproject.memoWithTags.aspect

import com.wafflestudio.toyproject.memoWithTags.user.AuthUser
import com.wafflestudio.toyproject.memoWithTags.user.controller.User
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDate

@Aspect
@Component
class ApiCallLoggingAspect {

    private val logger = LoggerFactory.getLogger(ApiCallLoggingAspect::class.java)

    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    fun logApiCall(joinPoint: JoinPoint) {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes ?.request
        val apiUrl = request?.requestURL ?: "Unknown API"
        val httpMethod = request?.method ?: "Unknown"

        var username = "anonymous"
        val methodSignature = joinPoint.signature as MethodSignature
        val parameterAnnotations = methodSignature.method.parameterAnnotations
        val args = joinPoint.args
        for (i in parameterAnnotations.indices) {
            for (annotation in parameterAnnotations[i]) {
                if (annotation is AuthUser) {
                    val userArg = args[i]
                    if (userArg is User) {
                        username = userArg.nickname
                    }
                }
            }
        }

        val today = LocalDate.now()
        logger.info("API call - User: $username, Date: $today, API: $httpMethod $apiUrl")
    }
}
