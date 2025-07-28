package com.wafflestudio.toyproject.memoWithTags.openai.service

import com.aallam.openai.api.embedding.EmbeddingRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class OpenAIService(
    private val openAI: OpenAI,
    private val objectMapper: ObjectMapper
) {

    suspend fun getEmbedding(text: String): String {
        val req = EmbeddingRequest(
            model = ModelId("text-embedding-ada-002"),
            input = listOf(text)
        )
        val res = openAI.embeddings(req)
        val floats = res.embeddings.first().embedding.map { it.toFloat() }

        return objectMapper.writeValueAsString(floats)
    }
}
