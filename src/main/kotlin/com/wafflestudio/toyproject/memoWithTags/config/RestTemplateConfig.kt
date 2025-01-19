package com.wafflestudio.toyproject.memoWithTags.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        val messageConverters: MutableList<HttpMessageConverter<*>> = mutableListOf()
        messageConverters.add(FormHttpMessageConverter())
        restTemplate.messageConverters = messageConverters

        return restTemplate
    }
}
