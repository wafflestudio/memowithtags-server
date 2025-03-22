package com.wafflestudio.toyproject.memoWithTags.config

import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.info.Info
import java.util.*
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.ErrorResponse
import org.springframework.web.method.HandlerMethod


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

}