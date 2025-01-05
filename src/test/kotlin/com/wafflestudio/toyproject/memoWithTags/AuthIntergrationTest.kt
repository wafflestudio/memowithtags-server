package com.wafflestudio.toyproject.memoWithTags

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthIntegrationTest
@Autowired
constructor(
    private val mvc: MockMvc,
    private val mapper: ObjectMapper
) {
    @Test
    fun `register API should return 201 Created`() {
        // Given: 요청 데이터 설정
        val request = mapOf(
            "email" to "team02@gmail.com",
            "password" to "spring"
        )
        val requestBody = mapper.writeValueAsString(request)

        // When & Then: API 호출 및 검증
        mvc.perform(
            post("/api/v1/auth/register") // POST 요청
                .contentType(MediaType.APPLICATION_JSON) // JSON 타입 지정
                .content(requestBody) // JSON 요청 본문 설정
        )
            .andExpect(status().isCreated) // 201 Created 응답 확인
    }
}
