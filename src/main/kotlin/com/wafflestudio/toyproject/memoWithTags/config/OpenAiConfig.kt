package com.wafflestudio.toyproject.memoWithTags.config

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import io.ktor.client.engine.okhttp.OkHttp
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.Duration.Companion.seconds

@Configuration
class OpenAiConfig(@Value("\${OPENAI_API_KEY}") private val apiKey: String) {
    @Bean
    fun openAI(): OpenAI {
        val config = OpenAIConfig(
            token = apiKey,
            timeout = Timeout(socket = 60.seconds),
            httpClientConfig = {
                engine { OkHttp.create() }
            }
        )
        return OpenAI(config)
    }
}
