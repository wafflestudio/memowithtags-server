package com.wafflestudio.toyproject.memoWithTags.config

import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ApiErrorCodeExamples
import com.wafflestudio.toyproject.memoWithTags.exception.annotation.ExplainError
import com.wafflestudio.toyproject.memoWithTags.exception.code.ErrorCode
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE


@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(configurationInfo())
    }

    private fun configurationInfo(): Info {
        return Info()
            .title("MemoWithTags API")
            .description("MemoWithTags API 명세서입니다.")
            .version("1.0.0")
    }

    @Bean
    fun customizeOperations(): OperationCustomizer {
        return OperationCustomizer { operation, handlerMethod ->
            val apiErrorCodeExamples = handlerMethod.method.getAnnotation(ApiErrorCodeExamples::class.java)

            // 메소드가 @ApiErrorCodeExamples를 가지고 있다면 예외 응답 코드를 추가
            if (apiErrorCodeExamples != null) {
                val errorDocsClass = apiErrorCodeExamples.value.java
                val instance = errorDocsClass.getDeclaredField("INSTANCE").apply {
                    isAccessible = true
                }.get(null)

                val errorCodes = errorDocsClass.declaredFields.mapNotNull { field ->
                    field.isAccessible = true
                    if (field.name == "INSTANCE") return@mapNotNull null

                    val explainError = field.getAnnotation(ExplainError::class.java) ?: return@mapNotNull null
                    val errorCode = field.get(instance) as? ErrorCode ?: return@mapNotNull null

                    Triple(field.name, errorCode, explainError.value)
                }

                // Group error codes by HTTP status
                val errorsByStatusCode = errorCodes.groupBy { (_, errorCode, _) ->
                    errorCode.status
                }

                // Add examples for each HTTP status
                errorsByStatusCode.forEach { (statusCode, errors) ->
                    val code = statusCode.toString()
                    val response = operation.responses[code] ?: ApiResponse()

                    val content = response.content ?: Content()
                    val mediaType = content.get(APPLICATION_JSON_VALUE)
                        ?: io.swagger.v3.oas.models.media.MediaType()

                    // Add each error as an example
                    errors.forEach { (fieldName, errorCode, explanation) ->
                        val example = Example().apply {
                            summary = errorCode.code
                            description = explanation
                            value = mapOf(
                                "status" to errorCode.status,
                                "code" to errorCode.code,
                                "message" to errorCode.message,
                            )
                        }

                        mediaType.addExamples(fieldName.toLowerCase(), example)
                    }

                    content.addMediaType(APPLICATION_JSON_VALUE, mediaType)
                    response.content = content
                    operation.responses.addApiResponse(code, response)
                }
            }

            operation
        }
    }



}