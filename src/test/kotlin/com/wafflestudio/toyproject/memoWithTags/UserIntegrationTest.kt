package com.wafflestudio.toyproject.memoWithTags

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
            "password" to "Password123!"
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
            "password" to "Password123!"
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

        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "Password123!"
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

    @Test
    fun `refresh token and delete user`() {
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
            "password" to "Password123!"
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

        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "Password123!"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val refreshToken = jsonNode.get("refreshToken").asText()

        assertNotNull(refreshToken)

        val refreshRequest = mapOf(
            "refreshToken" to refreshToken
        )

        val accessTokenResponse = mockMvc.perform(
            post("/api/v1/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(refreshRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andReturn()

        val accessToken = mapper.readTree(accessTokenResponse.response.contentAsString).get("accessToken").asText()

        val deleteUserRequest = mapOf(
            "email" to "test@example.com"
        )

        mockMvc.perform(
            delete("/api/v1/auth/withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $accessToken")
                .content(mapper.writeValueAsString(deleteUserRequest))
        )
            .andExpect(status().isNoContent)

        val loginRequest1 = mapOf(
            "email" to "test@example.com",
            "password" to "Password123!"
        )

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isUnauthorized)
    }
}
