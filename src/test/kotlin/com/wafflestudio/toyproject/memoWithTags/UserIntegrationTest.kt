package com.wafflestudio.toyproject.memoWithTags

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper()

    @Test
    fun `user register test`() {
        val mailRequest = mapOf(
            "email" to "test@example.com"
        )

        mockMvc.perform(
            post("/api/v1/auth/send-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(mailRequest))
        )
            .andExpect(status().isOk)

        val verifyRequest = mapOf(
            "email" to "test@example.com",
            "verificationCode" to "000000"
        )

        mockMvc.perform(
            post("/api/v1/auth/verify-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(verifyRequest))
        )
            .andExpect(status().isOk)

        val requestBody = mapOf(
            "email" to "test@example.com",
            "nickname" to "John Doe",
            "password" to "password"
        )

        val requestJson = mapper.writeValueAsString(requestBody)

        mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
    }

    @Test
    fun `user login`() {
        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
    }
}
